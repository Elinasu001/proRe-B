package com.kh.even.back.chat.model.service;


import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.kh.even.back.chat.model.dto.ChatMessageDTO;
import com.kh.even.back.chat.model.dto.ChatRoomDTO;
import com.kh.even.back.chat.model.vo.ChatMessageVO;
import com.kh.even.back.chat.model.vo.ChatRoomVO;

public interface ChatService {

    /**
     * 채팅방 생성
     */
    ChatRoomVO createRoom(ChatRoomDTO chatRoomDTO, ChatMessageDTO chatMessageDTO, List<MultipartFile> files, Long userNo);

    /**
     * 메시지 목록 조회 (페이징)
     */
    List<ChatMessageDTO> getMessages(Long roomNo, Long userNo, Long lastMessageNo, int size);

    /**
     * 채팅 메시지 저장 (웹소켓/파일용)
     */
    ChatMessageVO saveMessage(ChatMessageDTO chatMessageDTO, List<MultipartFile> files);

}
