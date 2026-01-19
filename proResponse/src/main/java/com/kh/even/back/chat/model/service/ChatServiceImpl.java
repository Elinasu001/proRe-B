package com.kh.even.back.chat.model.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kh.even.back.chat.model.dao.ChatMapper;
import com.kh.even.back.chat.model.dto.ChatDetailDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
	
    private final ChatMapper chatMapper;
    private final ChatValidator chatValidator;

    public List<ChatDetailDTO> getChatDetails(ChatDetailDTO chatDetailDTO, Long userNo){
        // chatValidator.validateChatDetailDTO(chatDetailDTO);
    }

}
