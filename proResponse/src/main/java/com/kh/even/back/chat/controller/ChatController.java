package com.kh.even.back.chat.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kh.even.back.auth.model.vo.CustomUserDetails;
import com.kh.even.back.chat.model.dto.ChatMessageDTO;
import com.kh.even.back.chat.model.dto.ChatMessageResponse;
import com.kh.even.back.chat.model.dto.ChatMessageSearchDTO;
import com.kh.even.back.chat.model.service.ChatService;
import com.kh.even.back.chat.model.vo.ChatMessageVO;
import com.kh.even.back.chat.model.vo.ChatRoomVO;
import com.kh.even.back.common.ResponseData;

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
    public ResponseEntity<ResponseData<ChatMessageResponse>> getMessages(
            @PathVariable("roomNo") Long roomNo,
            @ModelAttribute ChatMessageSearchDTO searchDto,
            @AuthenticationPrincipal CustomUserDetails user) {
        ChatMessageResponse result = chatService.getMessages(
            roomNo,
            searchDto,
            user.getUserNo()
        );
        return ResponseData.ok(result, "메시지를 조회했습니다.");
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
