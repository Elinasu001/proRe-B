package com.kh.even.back.chat.model.vo;

import java.sql.Date;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ChatRoomMessageAttachmentVO {
    private Long fileNo;				// 파일번호 (PK)
	private String originName;			// 원본 파일명
	private String filePath;			// 파일 경로
	private Date uploadDate;	        // 파일 업로드 날짜
	private String status;            	// STATUS (Y/N)
    private Long messageNo;             // 메시지 번호 (FK)
}
