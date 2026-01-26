package com.kh.even.back.review.model.vo;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ReviewVO {
	private Long reviewNo;              // 리뷰 번호 (PK)
	private String content;             // 리뷰 내용
	private Integer starScore;          // 별점 (1~5)
	private Date createDate;            // 리뷰 등록일
	private String status;              // 리뷰 상태
	private Long estimateNo;            // 견적 번호 (FK)
}
