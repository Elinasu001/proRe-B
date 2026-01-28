
package com.kh.even.back.chat.model.service;


import java.util.List;

import com.kh.even.back.chat.model.dto.ChatAttachmentDTO;
import com.kh.even.back.chat.model.dto.ChatMessageDTO;
import com.kh.even.back.chat.model.dto.ChatMessageResponse;
import com.kh.even.back.chat.model.dto.ChatMessageSearchDTO;
import com.kh.even.back.chat.model.vo.ChatMessageVO;
import com.kh.even.back.chat.model.vo.ChatRoomVO;
import com.kh.even.back.exception.ChatException;

public interface ChatService {


    /**
     * 채팅방 생성 및 초기 메시지 저장
     */
    ChatRoomVO createRoom(Long estimateNo, ChatMessageDTO chatMessageDto, Long userNo);

    /**
     * 채팅 메시지 조회 (커서 기반 페이징)
     * 무한 스크롤 방식으로 과거 메시지 조회
     * 
     * @param estimateNo 견적 번호
     * @param searchDto 검색 조건 (cursor, size)
     * @param userNo 조회 요청한 사용자 번호 (mine 여부 판단용)
     * @return 메시지 목록 및 페이징 정보
     */
    ChatMessageResponse getMessages(Long estimateNo,  ChatMessageSearchDTO searchDto, Long userNo);

    /**
     * 메시지 저장 (TEXT/FILE/PAYMENT)
     * WebSocket 텍스트 메시지 전송 시 호출
     * DB 저장만 수행하고 브로드캐스트는 WebSocketHandler에서 처리
     * 
     * @param estimateNo 견적 번호
     * @param chatMessageDto 메시지 정보 (content, type, files 등)
     * @param userNo 메시지 발신자 번호
     * @return 저장된 메시지 정보 (messageNo 포함)
     */
    ChatMessageDTO saveMessage(Long estimateNo, ChatMessageDTO chatMessageDto, Long userNo);

    
    
    /**
     * messageNo(메시지 PK)로 단일 메시지와 첨부파일(attachments)까지 조회
     * 실시간 브로드캐스트 등에서 방금 저장한 메시지 1건을 정확히 가져올 때 사용
     * @param messageNo 메시지 번호(PK)
     * @return 첨부파일 정보까지 포함된 메시지 DTO
     */
    ChatMessageDTO getMessageBymessageNo(Long messageNo);

    // 파일 URL만 반환
    List<ChatAttachmentDTO> getAttachmentsByMessageNo(Long messageNo);
    
    /**
     * 회원 번호로 닉네임 조회
     * WebSocket 메시지 전송 시 발신자 이름 표시용
     * 
     * @param userNo 회원 번호
     * @return 닉네임
     */
    String getNicknameByUserNo(Long userNo);

    /**
     * 견적 번호로 채팅방 번호 조회
     * estimateNo를 roomNo로 변환하여 실제 채팅방 식별
     * 
     * @param estimateNo 견적 번호
     * @return 채팅방 번호
     * @throws ChatException 채팅방이 존재하지 않는 경우
     */
    Long getRoomNoByEstimateNo(Long estimateNo);

}
