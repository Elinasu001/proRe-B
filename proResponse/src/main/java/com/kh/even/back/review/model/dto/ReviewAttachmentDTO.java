package com.kh.even.back.review.model.dto;

import java.sql.Date;

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
public class ReviewAttachmentDTO {
	private Long fileNo;               // 파일번호 (PK)
	private String originName;         // 원본 파일명
	private String filePath;           // 파일 경로
	private Date uploadDate;           // 파일 업로드 날짜
	private String status;             // STATUS (Y/N)
	private Long reviewNo;             // 리뷰 번호 (FK)
}
