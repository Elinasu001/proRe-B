package com.kh.even.back.chat.socket;

import java.io.IOException;
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
import com.kh.even.back.chat.model.dao.ChatMapper;
import com.kh.even.back.chat.model.dto.WebSocketMessageDTO;
import com.kh.even.back.chat.model.vo.ChatMessageVO;
import com.kh.even.back.chat.model.vo.ChatRoomMessageAttachmentVO;
import com.kh.even.back.chat.model.vo.ChatRoomVO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketChatHandler extends TextWebSocketHandler {
    
    // 채팅방별 세션 관리 (roomNo → Set<WebSocketSession>)
    private final Map<String, Set<WebSocketSession>> rooms = new ConcurrentHashMap<>();
    
    private final ChatMapper chatMapper;
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    
    /**
     * 웹소켓 연결 시
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("웹소켓 연결: {}", session.getId());
        
        // URI에서 roomNo 추출
        String roomNo = getRoomNo(session);
        
        if (roomNo != null) {
            // 채팅방 존재 여부 확인
            ChatRoomVO chatRoom = chatMapper.getChatRoomByRoomNo(Long.parseLong(roomNo));
            
            if (chatRoom == null) {
                log.warn("존재하지 않는 채팅방: {}", roomNo);
                session.close();
                return;
            }
            
            // 세션을 채팅방에 추가
            rooms.computeIfAbsent(roomNo, k -> ConcurrentHashMap.newKeySet())
                 .add(session);
            
            log.info("👥 채팅방 {} 입장 완료. 현재 인원: {}", roomNo, rooms.get(roomNo).size());
        }
    }
    
    
    /**
     * 메시지 수신 시
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        log.info("메시지 수신: {}", message.getPayload());
        
        String roomNo = getRoomNo(session);
        if (roomNo == null) return;
        
        try {
            // JSON → DTO 변환
            WebSocketMessageDTO wsMessage = objectMapper.readValue(
                message.getPayload(), 
                WebSocketMessageDTO.class
            );
            
            // 메시지 타입별 처리
            switch (wsMessage.getType()) {
                case "MESSAGE":
                    handleTextMessage(wsMessage, roomNo);
                    break;
                    
                case "FILE":
                    handleFileMessage(wsMessage, roomNo);
                    break;
                    
                case "ENTER":
                    handleEnterMessage(wsMessage, roomNo);
                    break;
                    
                case "LEAVE":
                    handleLeaveMessage(wsMessage, roomNo);
                    break;
                    
                default:
                    log.warn("알 수 없는 메시지 타입: {}", wsMessage.getType());
            }
            
        } catch (Exception e) {
            log.error("메시지 처리 실패", e);
        }
    }
    
    
    /**
     * 텍스트 메시지 처리
     */
    private void handleTextMessage(WebSocketMessageDTO wsMessage, String roomNo) {
        // 1. DB 저장
        ChatMessageVO messageVO = ChatMessageVO.builder()
            .roomNo(wsMessage.getRoomNo())
            .userNo(wsMessage.getUserNo())
            .content(wsMessage.getContent())
            .status("Y")
            .build();
        
        chatMapper.saveMessage(messageVO);
        
        // 2. 저장된 정보 추가
        wsMessage.setMessageNo(messageVO.getMessageNo());
        wsMessage.setSentDate(messageVO.getSentDate().toString());
        
        // 3. 브로드캐스트
        broadcast(roomNo, wsMessage);
        
        log.info("텍스트 메시지 저장 완료: messageNo={}", messageVO.getMessageNo());
    }
    
    
    /**
     * 파일 메시지 처리
     */
    private void handleFileMessage(WebSocketMessageDTO wsMessage, String roomNo) {
        // 1. 메시지 저장
        ChatMessageVO messageVO = ChatMessageVO.builder()
            .roomNo(wsMessage.getRoomNo())
            .userNo(wsMessage.getUserNo())
            .content(wsMessage.getContent())
            .status("Y")
            .build();
        
        chatMapper.saveMessage(messageVO);
        
        // 2. 첨부파일 저장
        ChatRoomMessageAttachmentVO attachmentVO = ChatRoomMessageAttachmentVO.builder()
            .messageNo(messageVO.getMessageNo())
            .originName(wsMessage.getOriginName())
            .filePath(wsMessage.getFilePath())
            .status("Y")
            .build();
        
        chatMapper.saveMessageAttachment(attachmentVO);
        
        // 3. 저장된 정보 추가
        wsMessage.setMessageNo(messageVO.getMessageNo());
        wsMessage.setSentDate(messageVO.getSentDate().toString());
        
        // 4. 브로드캐스트
        broadcast(roomNo, wsMessage);
        
        log.info("파일 메시지 저장 완료: messageNo={}, fileNo={}", 
                 messageVO.getMessageNo(), attachmentVO.getFileNo());
    }
    
    
    /**
     * 입장 메시지 처리
     */
    private void handleEnterMessage(WebSocketMessageDTO wsMessage, String roomNo) {
        // 입장 메시지는 DB에 저장하지 않고 브로드캐스트만
        broadcast(roomNo, wsMessage);
        log.info("{} 입장", wsMessage.getUserName());
    }
    
    
    /**
     * 퇴장 메시지 처리
     */
    private void handleLeaveMessage(WebSocketMessageDTO wsMessage, String roomNo) {
        // 퇴장 메시지는 DB에 저장하지 않고 브로드캐스트만
        broadcast(roomNo, wsMessage);
        log.info("{} 퇴장", wsMessage.getUserName());
    }
    
    
    /**
     * 같은 방의 모든 사용자에게 메시지 전송
     */
    private void broadcast(String roomNo, WebSocketMessageDTO wsMessage) {
        try {
            TextMessage textMessage = new TextMessage(
                objectMapper.writeValueAsString(wsMessage)
            );
            
            Set<WebSocketSession> sessions = rooms.getOrDefault(roomNo, Collections.emptySet());
            
            for (WebSocketSession session : sessions) {
                if (session != null && session.isOpen()) {
                    try {
                        session.sendMessage(textMessage);
                    } catch (IOException e) {
                        log.error("메시지 전송 실패: sessionId={}", session.getId(), e);
                    }
                }
            }
            
            log.info("브로드캐스트 완료: roomNo={}, 수신자={}", roomNo, sessions.size());
            
        } catch (Exception e) {
            log.error("브로드캐스트 실패", e);
        }
    }
    
    
    /**
     * 웹소켓 연결 종료 시
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("웹소켓 연결 종료: {} / {}", session.getId(), status);
        
        String roomNo = getRoomNo(session);
        
        if (roomNo != null) {
            Set<WebSocketSession> sessions = rooms.get(roomNo);
            
            if (sessions != null) {
                sessions.remove(session);
                
                // 방에 아무도 없으면 방 삭제
                if (sessions.isEmpty()) {
                    rooms.remove(roomNo);
                    log.info("빈 채팅방 제거: {}", roomNo);
                } else {
                    log.info("채팅방 {} 퇴장. 남은 인원: {}", roomNo, sessions.size());
                }
            }
        }
    }
    
    
    /**
     * URI에서 roomNo 추출
     * 예: /ws/chat/5 → "5"
     */
    private String getRoomNo(WebSocketSession session) {
        try {
            String path = session.getUri().getPath();
            String[] parts = path.split("/");
            
            if (parts.length >= 4) {
                return parts[3];  // /ws/chat/{roomNo}
            }
        } catch (Exception e) {
            log.error("roomNo 추출 실패", e);
        }
        
        return null;
    }
}