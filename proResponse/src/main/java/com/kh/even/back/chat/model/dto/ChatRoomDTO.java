package com.kh.even.back.chat.model.dto;


import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor 
@ToString
public class ChatRoomDTO {
    private Long roomNo;              // 채팅방 번호 (PK)
    private String status;            // 채팅방 상태
    private LocalDateTime createDate; // 생성일
    private Long estimateNo;          // 견적 번호 (FK)
    private Long userNo;              // 회원 번호 (FK)
    private String userName;          // 회원 이름
}
