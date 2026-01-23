package com.kh.even.back.chat.model.vo;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ChatRoomVO {
    private Long roomNo;				// 채팅방 번호 (PK)
    private String status;				// 상태
    private LocalDateTime createDate;	// 생성 일자
    private Long estimateNo;			// 견적 번호 (FK)
}
