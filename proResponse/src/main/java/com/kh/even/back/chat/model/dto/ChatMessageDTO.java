package com.kh.even.back.chat.model.dto;

import java.time.LocalDateTime;
import java.util.List;

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

    private Long messageNo;				// 메시지 번호 (PK)
    private String content;				// 메시지 내용
    private Long amount;                // 결제 메시지일 때만 값 (null 가능)

    private LocalDateTime sentDate;		// 보낸 시간

    private Long senderUserNo;          // 보낸 사람
    private boolean read;               // 읽음 여부 (status Y/N 변환)
    private boolean isMine;             // 내 메시지인지 (UI용)

    private List<ChatRoomMessageAttachmentDTO> attachments; // 첨부파일 목록
}
