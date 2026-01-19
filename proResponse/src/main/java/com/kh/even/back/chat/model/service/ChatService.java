package com.kh.even.back.chat.model.service;


import java.util.List;

import com.kh.even.back.chat.model.dto.ChatDetailDTO;

public interface ChatService {
    List<ChatDetailDTO> getChatDetails(Long roomNo, Long userNo);
}
