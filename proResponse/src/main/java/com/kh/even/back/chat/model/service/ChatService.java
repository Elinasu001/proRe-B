package com.kh.even.back.chat.model.service;


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
    ChatMessageVO saveMessage(Long estimateNo, ChatMessageDTO chatMessageDto, Long userNo);

    
    /**
     * 메시지 저장 + WebSocket 브로드캐스트
     * HTTP 파일 업로드 시 호출
     * DB 저장 후 실시간으로 다른 사용자들에게 알림 전송
     * 
     * @param estimateNo 견적 번호
     * @param chatMessageDto 메시지 정보 (파일 포함)
     * @param userNo 메시지 발신자 번호
     * @return 저장된 메시지 정보
     */
    ChatMessageVO saveMessageAndBroadcast(Long estimateNo, ChatMessageDTO chatMessageDto, Long userNo);
    
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
