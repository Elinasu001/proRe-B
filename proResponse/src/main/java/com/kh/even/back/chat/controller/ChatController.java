package com.kh.even.back.chat.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.even.back.auth.model.vo.CustomUserDetails;
import com.kh.even.back.chat.model.dto.ChatDetailDTO;
import com.kh.even.back.chat.model.service.ChatService;
import com.kh.even.back.common.ResponseData;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/rooms")
public class ChatController {
	
    private final ChatService chatService;

    @GetMapping({"/{roomNo}"})
    public ResponseData<List<ChatDetailDTO>> getChatDetails(
        @PathVariable(name="roomNo") Long roomNo,
        @AuthenticationPrincipal CustomUserDetails user) {
        List<ChatDetailDTO> chatDetails = chatService.getChatDetails(roomNo, user.getUserNo());
        return ResponseData.ok(chatDetails, "채팅방 상세 정보 조회 성공");
    }


}
