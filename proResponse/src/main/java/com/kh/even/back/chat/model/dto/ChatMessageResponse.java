package com.kh.even.back.chat.model.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChatMessageResponse {
    private Long cursor;
    private int requestedSize;
    private int size;
    private Long estimateNo;
    private List<ChatMessageDTO> messages;
}
