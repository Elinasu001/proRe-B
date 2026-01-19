package com.kh.even.back.chat.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/rooms")
public class ChatController {
	
    // private final ChatService chatService;

    // @GetMapping({"/{roomNo}"})
    // public List<ChatDetailDTO> getChatDetail(@PathVariable("roomNo") int roomNo) {
    //     //log.info("Fetching chat messages for room: " + roomNo);
    //     return chatService.getChatDetail(roomNo);
    // }   


}
