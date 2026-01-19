package com.kh.even.back.review.model.vo;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ReviewMapVO {
    private Long reviewNo;      // 리뷰번호 (PK, FK)
    private Integer tagNo;      // 태그번호 (PK, FK)
}
