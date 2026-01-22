package com.kh.even.back.chat.model.vo;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ChatMessageVO {
    private Long messageNo;				// 메시지 번호 (PK)
    private String content;				// 메시지 내용
    private LocalDateTime sentDate;		// 보낸 시간
    private String status;				// 확인 여부
    private String type;
    private Long userNo;				// 회원 번호 (FK)
    private Long roomNo;				// 채팅방 번호 (FK)
}
