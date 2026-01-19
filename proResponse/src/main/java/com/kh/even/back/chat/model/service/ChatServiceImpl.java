package com.kh.even.back.chat.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.kh.even.back.chat.model.dao.ChatMapper;
import com.kh.even.back.chat.model.dto.ChatMessageDTO;
import com.kh.even.back.chat.model.dto.ChatRoomDTO;
import com.kh.even.back.chat.model.dto.ChatRoomUserDTO;
import com.kh.even.back.file.service.S3Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatServiceImpl  implements ChatService {

    private final ChatMapper chatMapper;
    private final S3Service s3Service;

    @Override
    public ChatRoomDTO getChatRoom(Long roomNo, Long userNo) {

        ChatRoomUserDTO dto = new ChatRoomUserDTO();
        dto.setRoomNo(roomNo);
        dto.setUserNo(userNo);

        return chatMapper.getChatRoom(dto);
    }
    
    /**
     * 메시지 목록 조회 (페이징)
     */
    @Override
    public List<ChatMessageDTO> getMessages(
            Long roomNo,
            Long userNo,
            Long lastMessageNo,
            int size) {

        Map<String, Object> param = new HashMap<>();
        param.put("roomNo", roomNo);
        param.put("lastMessageNo", lastMessageNo);
        param.put("size", size);

        List<ChatMessageDTO> messages = chatMapper.getMessages(param);

        for (ChatMessageDTO message : messages) {
            message.setMine(
                message.getSenderUserNo() != null &&
                message.getSenderUserNo().equals(userNo)
            );
            message.setRead(true);
        }

        return messages;
    }


}