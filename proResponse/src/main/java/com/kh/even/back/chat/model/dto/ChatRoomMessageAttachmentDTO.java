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
public class ChatRoomMessageAttachmentDTO {
	private Long fileNo;				// 파일번호 (PK)
	private String originName;			// 원본 파일명
	private String filePath;			// 파일 경로
	private LocalDateTime  uploadDate;	// 파일 업로드 날짜
	private String status;            	// STATUS (Y/N)
    private Long messageNo;             // 메시지 번호 (FK)
}
