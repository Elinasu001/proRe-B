package com.kh.even.back.review.model.vo;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ReviewVO {
	private Long reviewNo;				// 리뷰 번호 (PK)
	private String content;				// 리뷰 내용
	private Integer starScore;			// 별점 (1~5)
	private LocalDateTime createDate;	// 리뷰 등록일
	private String status;				// 리뷰 상태
	private Long estimateNo;			// 견적 번호 (FK)
}
