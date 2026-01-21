package com.kh.even.back.chat.model.service;


import java.util.List;

import com.kh.even.back.chat.model.dto.ChatMessageDTO;
import com.kh.even.back.chat.model.dto.ChatRoomDTO;

public interface ChatService {
    
    /**
     * 채팅방 상세 조회
     */
    ChatRoomDTO getChatRoom(Long roomNo, Long userNo);

    /**
     * 메시지 목록 조회 (페이징)
     */
    List<ChatMessageDTO> getMessages(Long roomNo, Long userNo, Long lastMessageNo, int size);
  
}
