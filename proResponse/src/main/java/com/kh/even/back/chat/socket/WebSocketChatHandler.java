package com.kh.even.back.chat.socket;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kh.even.back.chat.model.dto.ChatMessageDTO;
import com.kh.even.back.chat.model.service.ChatService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketChatHandler extends TextWebSocketHandler {
    
      private final Map<String, Set<WebSocketSession>> rooms = new ConcurrentHashMap<>();
      private final ChatService chatService;
      private final ObjectMapper objectMapper = new ObjectMapper();

      // 웹소켓 연결 시 호출
      @Override
      public void afterConnectionEstablished(WebSocketSession session) throws Exception {
         log.info("웹소켓 연결 성공");
         log.info("{}", session);
         String roomId = getRoomId(session);
         if (roomId != null) {
            rooms.computeIfAbsent(roomId, k -> ConcurrentHashMap.newKeySet()).add(session);
         }
      }

      // 메시지 수신 시 호출
      @Override
      protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
         log.info("메시지 수신: {} / {}", session, message);
         String roomId = getRoomId(session);
         if (roomId == null) return;

         ChatMessageDTO chatMessage = objectMapper.readValue(message.getPayload(), ChatMessageDTO.class);
         // DB 저장 (Service 사용)
         chatService.saveMessage(chatMessage, null);

         TextMessage textMessage = new TextMessage(objectMapper.writeValueAsString(chatMessage));
         for (WebSocketSession user : rooms.getOrDefault(roomId, Collections.emptySet())) {
            if (user != null && user.isOpen()) {
               user.sendMessage(textMessage);
            }
         }
      }

      // 연결 해제 시 호출
      @Override
      public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
         log.info("웹소켓 연결 해제: {} / {}", session, status);
         String roomId = getRoomId(session);
         if (roomId != null) {
            Set<WebSocketSession> sessions = rooms.get(roomId);
            if (sessions != null) {
               sessions.remove(session);
               if (sessions.isEmpty()) {
                  rooms.remove(roomId);
               }
            }
         }
      }

      // 세션에서 roomId 추출 (URI 경로 기반)
      private String getRoomId(WebSocketSession session) {
         String path = session.getUri().getPath();
         String[] part = path.split("/");
         if (part.length > 3) {
            return part[3];
         }
         return null;
      }

}