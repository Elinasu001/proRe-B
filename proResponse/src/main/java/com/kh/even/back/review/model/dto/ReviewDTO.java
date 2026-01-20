package com.kh.even.back.review.model.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class ReviewDTO {

    @NotNull(message = "견적 번호는 필수입니다")
    private Long estimateNo;
    
    @NotBlank(message = "리뷰 내용은 필수입니다")
    @Size(max = 2000, message = "리뷰는 2000자 이내로 작성해주세요")
    private String content;
    
    @NotNull(message = "별점은 필수입니다")
    @Min(value = 1, message = "별점은 1점 이상이어야 합니다")
    @Max(value = 5, message = "별점은 5점 이하여야 합니다")
    private Integer starScore;
    
    // 선택된 태그 번호 목록
    @Size(max = 4, message = "태그는 최대 4개까지 선택 가능합니다")
    private List<Long> tagNos;
    
    // 첨부파일
    private List<MultipartFile> files;
    
}
