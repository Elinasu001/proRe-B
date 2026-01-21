package com.kh.even.back.chat.model.dto;


import java.sql.Date;

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
    private Long roomNo;
    private String status;
    private Date createdDate;
    private Long estimateNo;
    private Long userNo;   // 상대방 정보 (JOIN)
    private String userName;
}
