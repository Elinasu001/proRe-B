package com.kh.even.back.review.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.kh.even.back.review.model.dto.ReviewDetailDTO;
import com.kh.even.back.review.model.dto.ReviewTagDTO;
import com.kh.even.back.review.model.vo.ReviewAttachmentVO;
import com.kh.even.back.review.model.vo.ReviewMapVO;
import com.kh.even.back.review.model.vo.ReviewVO;

@Mapper
public interface ReviewMapper {

    /**
     * 견적 번호 기준 리뷰 상세 조회
     */
    ReviewDetailDTO getByEstimateNo(Long estimateNo);

    /**
     * 전체 태그 목록 조회
     */
    List<ReviewTagDTO> getReviewTags();

    /**
     * 견적 번호로 리뷰 조회
     */
    boolean existsReviewByEstimateNo(Long estimateNo);

    // === 리뷰 등록 ===
    /**
     * 리뷰 등록
     */
    int saveReview(ReviewVO reviewVO);
    
    /**
     * 리뷰 첨부파일 등록
     */
    int saveReviewAttachment(ReviewAttachmentVO attachmentVO);
    
    /**
     * 리뷰 태그 매핑 등록
     */
    int saveReviewMap(ReviewMapVO reviewMapVO);


    /**
     * 견적 번호로 리뷰 조회 (리뷰 상태변경용)
     */
    ReviewVO getReviewByEstimateNo(Long estimateNo);

    /**
     * 리뷰 상태 변경(비활성화)
     */
    int updateReviewStatus(Long reviewNo);

}
