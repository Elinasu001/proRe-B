package com.kh.even.back.chat.model.service;


import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.kh.even.back.chat.model.dto.ChatMessageDTO;
import com.kh.even.back.chat.model.dto.ChatRoomDTO;
import com.kh.even.back.chat.model.vo.ChatMessageVO;
import com.kh.even.back.chat.model.vo.ChatRoomVO;

public interface ChatService {


    /**
     * 채팅방 생성 및 초기 메시지 저장
     */
    ChatRoomVO createRoom(ChatRoomDTO chatRoomDto, ChatMessageDTO chatMessageDto, List<MultipartFile> files, Long userNo);

    /**
     * 채팅 메시지 조회 (커서 기반 페이징)
     */
    List<ChatMessageDTO> getMessages(Long roomNo, Long userNo, Long messageNo, int size);

    /**
     * 메시지 저장 (TEXT/FILE/PAYMENT)
     */
    ChatMessageVO saveMessage(ChatMessageDTO chatMessageDto, List<MultipartFile> files);

    /**
     * 회원 번호로 닉네임 조회
     */
    String getNicknameByUserNo(Long userNo);

    /**
     * 견적 번호로 채팅방 번호 조회
     */
    Long getRoomNoByEstimateNo(Long estimateNo);


}
