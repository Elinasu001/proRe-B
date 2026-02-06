package com.kh.even.back.chat.model.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ChatMessageResponse {
    private Long cursor;
    private int requestedSize;
    private int size;
    private Long estimateNo;
    private List<ChatMessageDTO> messages;
    private Long nextCursor; // 다음 페이지 요청에 사용할 커서
}
