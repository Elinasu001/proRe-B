package com.kh.even.back.chat.model.service;


import com.kh.even.back.chat.model.dto.ChatMessageDTO;
import com.kh.even.back.chat.model.dto.ChatMessageResponse;
import com.kh.even.back.chat.model.dto.ChatMessageSearchDTO;
import com.kh.even.back.chat.model.vo.ChatMessageVO;
import com.kh.even.back.chat.model.vo.ChatRoomVO;

public interface ChatService {


    /**
     * 채팅방 생성 및 초기 메시지 저장
     */
    ChatRoomVO createRoom(Long estimateNo, ChatMessageDTO chatMessageDto, Long userNo);

    /**
     * 채팅 메시지 조회 (커서 기반 페이징)
     */
    //List<ChatMessageDTO> getMessages(Long roomNo,  ChatMessageDTO chatMessageDto, Long userNo);
    ChatMessageResponse getMessages(Long roomNo,  ChatMessageSearchDTO searchDto, Long userNo);

    /**
     * 메시지 저장 (TEXT/FILE/PAYMENT)
     */
    ChatMessageVO saveMessage(ChatMessageDTO chatMessageDto, Long userNo);

    /**
     * 회원 번호로 닉네임 조회
     */
    String getNicknameByUserNo(Long userNo);

    /**
     * 견적 번호로 채팅방 번호 조회
     */
    Long getRoomNoByEstimateNo(Long estimateNo);

    // ChatMessageDTO getMessageByNo(Long messageNo); // 단일 메시지 조회는 필요 없으므로 삭제
}
