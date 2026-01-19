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
public class ReviewTagVO {
    private Integer tagNo;  // 태그번호 (PK)
    private String tagName; // 태그명 (예: "경험과 노하우가 많아요", "시간을 잘 지켜요", "응대가 친절해요", "의사소통이 원활해요", "상담이 자세해요")
}