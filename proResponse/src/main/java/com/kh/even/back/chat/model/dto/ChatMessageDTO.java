package com.kh.even.back.chat.model.dto;

import java.time.LocalDateTime;

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
public class ChatMessageDTO {
    private Long messageNo;				// 메시지 번호
    private String content;				// 메시지 내용
    private LocalDateTime sentDate;		// 보낸 시간
    private String status;				// 확인 여부
    private Long roomNo;				// 채팅방 번호
    private Long userNo;				// 회원 번호
}
