package com.kh.even.back.chat.model.vo;

import java.sql.Date;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ChatRoomVO {
    private Long roomNo;				// 채팅방 번호 (PK)
    private String status;				// 상태
    private Date createdDate;	        // 생성 일자
    private Long estimateNo;			// 견적 번호 (FK)
}
