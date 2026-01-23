package com.kh.even.back.chat.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
import com.kh.even.back.chat.model.vo.ChatMessageVO;
import com.kh.even.back.chat.model.vo.ChatRoomVO;
import com.kh.even.back.common.ResponseData;
import com.kh.even.back.review.model.vo.ReviewVO;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.val;
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
            @RequestParam("estimateNo") Long estimateNo,
            @ModelAttribute ChatMessageDTO chatMessageDto,
            @AuthenticationPrincipal CustomUserDetails user) {
        ChatRoomVO chatRoom = chatService.createRoom(estimateNo, chatMessageDto, user.getUserNo());
        return ResponseData.ok(chatRoom, "채팅방이 생성되었습니다.");
    }


    /**
     * 채팅 메시지 조회 (커서 기반 페이징)
     */
    @GetMapping("/{roomNo}/messages")
    public ResponseEntity<ResponseData<List<ChatMessageDTO>>> getMessages(
            @PathVariable("roomNo") Long roomNo,
            @Valid ChatMessageDTO chatMessageDto,
            @AuthenticationPrincipal CustomUserDetails user) {
        List<ChatMessageDTO> messages = chatService.getMessages(
            roomNo,
            chatMessageDto,
            user.getUserNo()
        );
        return ResponseData.ok(messages, "메시지를 조회했습니다.");
    }


    /**
     * 기존 채팅방에 파일/텍스트 메시지 전송
     * userNo/content/type
     */
    @PostMapping("/{roomNo}")
    public ResponseEntity<ResponseData<ChatMessageVO>> saveMessage(
            @ModelAttribute ChatMessageDTO chatMessageDto,
            @AuthenticationPrincipal CustomUserDetails user) {

        ChatMessageVO saved = chatService.saveMessage(
                chatMessageDto,
                user.getUserNo()
        );

        return ResponseData.ok(saved, "파일이 전송되었습니다.");
    }
    


}
