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
    private Long reviewNo;
    private String content;
    private Integer starScore;
    private Date createDate;
    private Long estimateNo;

    // 첨부파일 목록
    private List<ReviewAttachmentDTO> attachments;
    
    // 선택된 태그 목록
    private List<ReviewTagDTO> selectedTags;
}
