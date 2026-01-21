package com.kh.even.back.chat.model.dto;

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
public class ChatRoomUserDTO {
    private Long roomNo;	  // 채팅방 번호
    private Long userNo;	  // 회원 번호
}
