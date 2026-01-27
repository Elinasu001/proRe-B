package com.kh.even.back.review.model.dto;

import java.sql.Date;
import java.util.List;

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
public class ReviewDetailDTO {
    private Long reviewNo;          // 리뷰번호 (PK)
    private String content;         // 리뷰 내용
    private Integer starScore;      // 별점
    private Date createDate;        // 리뷰 작성 일자
    private Long estimateNo;        // 견적서 번호 (FK)
    
    private String nickName;       // 작성자 닉네임
    private String categoryName;   // 카테고리 이름
    private String createAgo;      // "2주 전"

    // 첨부파일 목록
    private List<ReviewAttachmentDTO> attachments;

    
    // 선택된 태그 목록
    private List<ReviewTagDTO> selectedTags;
}
