package com.kh.even.back.review.model.dto;


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
public class ReviewTagDTO {
    private Integer tagNo;  // 태그번호 (PK)
    private String tagName; // 태그명 (예: "경험과 노하우가 많아요", "시간을 잘 지켜요", "응대가 친절해요", "의사소통이 원활해요", "상담이 자세해요")

    // 프론트엔드용 (리뷰 작성 시)
    private Boolean selected;  // 이 태그가 선택되었는지
}
