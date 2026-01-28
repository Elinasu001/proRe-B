package com.kh.even.back.chat.controller;

import java.util.List;

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
    @GetMapping("/{estimateNo}/messages")
    public ResponseEntity<ResponseData<ChatMessageResponse>> getMessages(
            @PathVariable("estimateNo") Long estimateNo,
            @ModelAttribute ChatMessageSearchDTO searchDto,
            @AuthenticationPrincipal CustomUserDetails user) {
        ChatMessageResponse result = chatService.getMessages(
            estimateNo,
            searchDto,
            user.getUserNo()
        );
        return ResponseData.ok(result, "메시지를 조회했습니다.");
    }


    /**
     * 기존 채팅방에 파일/텍스트 메시지 전송
     * userNo/content/type
     */
    @PostMapping("/{estimateNo}")
    public ResponseEntity<ResponseData<ChatMessageDTO>> saveMessage(
            @PathVariable("estimateNo") Long estimateNo,
            @ModelAttribute ChatMessageDTO chatMessageDto,
            @AuthenticationPrincipal CustomUserDetails user) {

        ChatMessageDTO saved = chatService.saveMessage(
                estimateNo,
                chatMessageDto,
                user.getUserNo()
        );
        
        return ResponseData.ok(saved, "메시지가 전송되었습니다.");
    }

    // 파일 처리용을 만들어서
    // 응답할 때 : (fileURL, message)

    // @PostMapping("/{estimateNo}/filePaths")
    // public ResponseEntity<List<String>> getFileUrls(
    //         @PathVariable("estimateNo") Long estimateNo,
    //         @RequestParam("messageNo") Long messageNo
    // ) {
    //     List<String> urls = chatService.getFileUrlsByMessageNo(messageNo);
    //     return ResponseEntity.ok(urls);
    // }
}
