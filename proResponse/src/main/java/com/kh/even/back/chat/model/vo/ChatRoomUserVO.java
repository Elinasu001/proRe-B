package com.kh.even.back.chat.model.vo;

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
public class ChatRoomUserVO {
  private Long roomNo;	// 채팅방 번호
  private Long userNo;	// 회원 번호
}
