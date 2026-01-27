package com.kh.even.back.review.model.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.kh.even.back.review.model.dto.ReviewDTO;
import com.kh.even.back.review.model.dto.ReviewDetailDTO;
import com.kh.even.back.review.model.dto.ReviewTagDTO;
import com.kh.even.back.review.model.vo.ExpertReviewVO;
import com.kh.even.back.review.model.vo.ReviewVO;
import com.kh.even.back.util.model.dto.PageResponse;

public interface ReviewService {

    /**
     * 견적 번호 기준 리뷰 상세 조회
     * @param estimateNo
     * @return 리뷰 상세 DTO(ReviewDetailDTO)
     */
    ReviewDetailDTO getReview(Long estimateNo, Long userNo);

    /**
     * 전체 태그 목록 조회 (등록 시 필요)
     * @return 태그 DTO 리스트(List<ReviewTagDTO>)
     */
	List<ReviewTagDTO> getAllReviewTags();

    /**
     * 리뷰 등록
     * @param reviewDTO 리뷰 DTO
     * @param files
     * @return 등록된 리뷰 VO(ReviewVO)
     */
    ReviewVO saveReview(ReviewDTO reviewDTO, List<MultipartFile> files,  Long userNo);

    /**
     * 리뷰 삭제 (논리삭제)
     * @param estimateNo
     */
    ReviewVO deleteByEstimateNo(Long estimateNo, Long userNo);
    
    /**
     * 
     * @param expertNo 조회할 전문가 번호
     * @param pageNo 페이지 넘버
     * @return 조회한 리뷰들
     */
    PageResponse<ExpertReviewVO> getExpertReviews(Long expertNo, int pageNo);
    
}
