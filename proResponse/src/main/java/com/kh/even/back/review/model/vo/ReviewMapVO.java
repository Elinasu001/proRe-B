package com.kh.even.back.review.model.vo;

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
public class ReviewMapVO {
    private Long reviewNo;      // 리뷰번호 (PK, FK)
    private Integer tagNo;      // 태그번호 (PK, FK)
}
