package com.kh.even.back.chat.model.vo;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ChatRoomUserVO {
  private Long roomNo;	// 채팅방 번호
  private Long userNo;	// 회원 번호
}
