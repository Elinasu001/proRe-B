package com.kh.even.back.chat.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ChatMessageSearchDTO {
    private Long messageNo;				// 메시지 번호 (PK)
    private int size = 50;
}
