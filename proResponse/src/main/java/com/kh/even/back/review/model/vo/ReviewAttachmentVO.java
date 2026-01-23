package com.kh.even.back.review.model.vo;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ReviewAttachmentVO {
	private Long fileNo;				// 파일번호 (PK)
	private String originName;			// 원본 파일명
	private String filePath;			// 파일 경로
	private LocalDateTime uploadDate;	        // 파일 업로드 날짜
	private String status;            	// STATUS (Y/N)
    private Long reviewNo;				// 리뷰 번호 (FK)
}
