package com.kh.even.back.review.model.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.kh.even.back.review.model.dto.ExpertReviewDTO;
import com.kh.even.back.review.model.dto.ReviewDetailDTO;
import com.kh.even.back.review.model.dto.ReviewTagDTO;
import com.kh.even.back.review.model.vo.ReviewAttachmentVO;
import com.kh.even.back.review.model.vo.ReviewMapVO;
import com.kh.even.back.review.model.vo.ReviewVO;

@Mapper
public interface ReviewMapper {

    // === 리뷰 조회 ===

    /**
     * 견적 번호 기준 리뷰 상세 조회
     * @param estimateNo
     * @return 리뷰 상세 DTO(ReviewDetailDTO)
     */
    ReviewDetailDTO getByEstimateNo(Long estimateNo);

    

    /**
     * 전체 태그 목록 조회
     * @return 태그 DTO 리스트(List<ReviewTagDTO>)
     */
    List<ReviewTagDTO> getAllReviewTags();

    /**
     * 견적 번호로 리뷰 존재 여부 확인
     * @param estimateNo 견적 번호
     * @return 존재 여부(boolean)
     */
    boolean existsByEstimateNo(Map<String, Object> params);

    // === 리뷰 등록 ===
    /**
     * ROLE_USER 확인
     * @param userNo
    */
    String getUserRoleByUserNo(Long userNo);
    /**
     * 리뷰 등록
     * @param reviewVO 리뷰 VO
     * @return 등록된 행 수(int)
     */
    int saveReview(ReviewVO reviewVO);
    
    /**
     * 리뷰 첨부파일 등록
     * @param reviewAttachmentVO 리뷰 첨부파일 VO
     * @return 등록된 행 수(int)
     */
    int saveReviewAttachment(ReviewAttachmentVO reviewAttachmentVO);
    
    //int saveReviewAttachment(FileVO fileVo);

    /**
     * 리뷰 태그 매핑 등록
     * @param reviewMapVO
     */
    int saveReviewMap(ReviewMapVO reviewMapVO);

    // === 리뷰 삭제 ===

    /**
     * 견적 번호로 리뷰 조회 (리뷰 상태변경용)
     * @param estimateNo
     */
    ReviewVO getReviewByEstimateNo(Long estimateNo);

    /**
     * 리뷰 상태 변경
     * @param reviewNo
     */
    //int updateReviewStatusByReviewNo(Long reviewNo);


    /**
     * 리뷰 삭제
     * @param reviewNo
     */
    int deleteReviewMapByReviewNo(Long reviewNo);
    int deleteReviewByReviewNo(Long reviewNo);
    int deleteAttachmentsByReviewNo(Long reviewNo);
    /**
     * 
     * @param expertNo
     * @return 리뷰수
     */
    int getReviewsCountByExpertNo(Long expertNo);
    
    List<ExpertReviewDTO> getExpertReviews(Long expertNo);
    
}