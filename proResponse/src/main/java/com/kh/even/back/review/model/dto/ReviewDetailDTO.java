package com.kh.even.back.review.model.dto;

import java.time.LocalDateTime;
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
    private Long reviewNo;
    private String content;
    private Integer starScore;
    private LocalDateTime createDate;
    private String status;
    private Long estimateNo;

    // 첨부파일 목록
    private List<ReviewAttachmentDTO> attachments;
    
    // 선택된 태그 목록
    private List<ReviewTagDTO> tags;
    
    // 계산 필드 (프론트엔드용)
    private Boolean hasReview;      // 리뷰 존재 여부
    private Boolean canWriteReview; // 리뷰 작성 가능 여부
    private String message;         // 안내 메시지
}
