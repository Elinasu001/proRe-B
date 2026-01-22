package com.kh.even.back.chat.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kh.even.back.auth.model.vo.CustomUserDetails;
import com.kh.even.back.chat.model.dto.ChatMessageDTO;
import com.kh.even.back.chat.model.dto.ChatRoomDTO;
import com.kh.even.back.chat.model.service.ChatService;
import com.kh.even.back.chat.model.vo.ChatRoomVO;
import com.kh.even.back.common.ResponseData;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class ChatController {
	
    private final ChatService chatService;

    /**
     * 채팅방 기본 정보 조회
     */
    @PostMapping
    public ResponseEntity<ResponseData<ChatRoomVO>> createRoom(
            @Valid ChatRoomDTO chatRoomDTO,
            @Valid ChatMessageDTO chatMessageDTO,
            @RequestParam(value = "files", required = false) List<MultipartFile> files,
            @AuthenticationPrincipal CustomUserDetails user) {
        ChatRoomVO chatRoom = chatService.createRoom(chatRoomDTO, chatMessageDTO, files, user.getUserNo());
        return ResponseData.ok(chatRoom, "채팅방이 생성되었습니다.");
    }

    /**
     * 메시지 목록 조회
     */
    @GetMapping("/{roomNo}/messages")
    public ResponseEntity<ResponseData<List<ChatMessageDTO>>> getMessages(
            @PathVariable Long roomNo,
            @RequestParam(required = false) Long lastMessageNo,
            @RequestParam(defaultValue = "50") int size,
            @AuthenticationPrincipal CustomUserDetails user) {
        
        List<ChatMessageDTO> messages = chatService.getMessages(roomNo, user.getUserNo(), lastMessageNo, size);
        return ResponseData.ok(messages, "메시지 목록 조회 성공");
    }


}
