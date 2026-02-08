package com.kh.even.back.chat.socket;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kh.even.back.chat.model.dto.ChatAttachmentDTO;
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

      /**
       * 연결 성공 시 호출
       */
      @Override
      public void afterConnectionEstablished(WebSocketSession session) throws Exception {
         log.info("웹소켓 연결 성공");
         log.info("{}", session);
         String estimateNo = getEstimateNo(session);
         if (estimateNo != null) {
               estimateRooms.computeIfAbsent(estimateNo, k -> ConcurrentHashMap.newKeySet()).add(session);
         }
      }

      /**
       * 메시지 수신 시 호출
       */
      @Override
      protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

         //log.info("메시지 수신: {}", message.getPayload());
   
         ObjectMapper om = new ObjectMapper();
         om.registerModule(new JavaTimeModule());

         String estimateNo = getEstimateNo(session);
         if (estimateNo == null) return;

         ChatMessageDTO chatMessage = om.readValue(message.getPayload(), ChatMessageDTO.class);

         // if (chatMessage.getUserNo() == null) {
         //    log.error("userNo가 null입니다! 프론트엔드에서 userNo를 포함해서 보내는지 확인하세요.");
         //    return;
         // }
         
         Long estimateNoLong = Long.parseLong(estimateNo);

         ChatMessageDTO messageToSend;
         
         // FILE 타입은 이미 REST API에서 저장됨 > 브로드캐스트만
         if ("FILE".equals(chatMessage.getType())) {
            messageToSend = chatMessage;  // 프론트에서 받은 데이터 그대로 사용
            //log.info("[WebSocket] FILE 메시지 브로드캐스트: {}", messageToSend);
         } else {
            // TEXT, PAYMENT 등은 DB 저장 후 브로드캐스트
            ChatMessageDTO saved = chatService.saveMessage(estimateNoLong, chatMessage, chatMessage.getUserNo());
            List<ChatAttachmentDTO> attachments = chatService.getAttachmentsByMessageNo(saved.getMessageNo());
            saved.setAttachments(attachments);
            messageToSend = saved;
            //log.info("[WebSocket] TEXT/PAYMENT 메시지 저장 및 브로드캐스트: {}", messageToSend);
         }

         // 브로드캐스트
         TextMessage textMessage = new TextMessage(om.writeValueAsString(messageToSend));
         for (WebSocketSession user : estimateRooms.getOrDefault(estimateNo, Collections.emptySet())) {
            if (user != null && user.isOpen()) {
               user.sendMessage(textMessage);
            }
         }
      }

      /**
       * 연결 종료 시 호출
       */
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

      /**
       * 세션에서 estimateNo 추출 (URI 경로 기반)
       */
      private String getEstimateNo(WebSocketSession session) {
          String path = session.getUri().getPath();
          String[] part = path.split("/");
          // 마지막 부분이 방 번호
          String estimateNo = part[part.length - 1];
          return estimateNo;
      }
}