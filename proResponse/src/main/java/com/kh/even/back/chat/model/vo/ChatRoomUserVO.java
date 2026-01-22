package com.kh.even.back.chat.model.vo;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ChatRoomUserVO {
    private Long userNo;
    private Long roomNo;
}
