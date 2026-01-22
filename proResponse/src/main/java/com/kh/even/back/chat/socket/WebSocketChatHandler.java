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
    
      private final Map<String, Set<WebSocketSession>> estimateRooms = new ConcurrentHashMap<>();
      private final ChatService chatService;
      // private final ObjectMapper objectMapper = new ObjectMapper();

      // 웹소켓 연결 시 호출
      @Override
      public void afterConnectionEstablished(WebSocketSession session) throws Exception {
         log.info("웹소켓 연결 성공");
         log.info("{}", session);
         String estimateNo = getEstimateNo(session);
         if (estimateNo != null) {
            estimateRooms.computeIfAbsent(estimateNo, k -> ConcurrentHashMap.newKeySet()).add(session);
         }
      }

      // 메시지 수신 시 호출
      @Override
      protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
         
         log.info("메시지 수신: {} / {}", session, message);

         ObjectMapper om = new ObjectMapper();

         String estimateNo = getEstimateNo(session);
         if (estimateNo == null) return;

         ChatMessageDTO chatMessage = om.readValue(message.getPayload(), ChatMessageDTO.class);
         
         // estimateNo로 roomNo 조회 후 설정
         Long roomNo = chatService.getRoomNoByEstimateNo(Long.parseLong(estimateNo));
         chatMessage.setRoomNo(roomNo);

         // DB 저장
         chatService.saveMessage(chatMessage, null);

         // 닉네임 조회 후 sender 세팅
         String nickname = chatService.getNicknameByUserNo(chatMessage.getUserNo());
         chatMessage.setSender(nickname);

         // 메시지 브로드캐스트
         TextMessage textMessage = new TextMessage(om.writeValueAsString(chatMessage));
                                                   // get을 했더니 Null이면 Default를 돌림. 이 경우엔 빈 Set을 돌리게 끔 만들어서 NullpointerException을 방지
         for (WebSocketSession user : estimateRooms.getOrDefault(estimateNo, Collections.emptySet())) {
            if (user != null && user.isOpen()) {
               user.sendMessage(textMessage);
            }
         }
      }

      // 연결 해제 시 호출
      @Override
      public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
         log.info("웹소켓 연결 해제: {} / {}", session, status);
         String estimateNo = getEstimateNo(session);
         if (estimateNo != null) {
            Set<WebSocketSession> sessions = estimateRooms.get(estimateNo);
            if (sessions != null) {
               sessions.remove(session);
               if (sessions.isEmpty()) {
                  estimateRooms.remove(estimateNo);
               }
            }
         }
      }

      // 세션에서 estimateNo 추출 (URI 경로 기반)
      private String getEstimateNo(WebSocketSession session) {
         String path = session.getUri().getPath();
         String[] part = path.split("/");
         String estimateNo = part[3];
         return estimateNo;
      }

}