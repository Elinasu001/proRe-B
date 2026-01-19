package com.kh.even.back.chat.model.vo;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ChatRoomVO {
    private Long roomNo;				// 채팅방 번호 (PK)
    private String status;				// 상태
    private LocalDateTime createdDate;	// 생성 일자
    private Long estimateNo;			// 견적 번호 (FK)
}
