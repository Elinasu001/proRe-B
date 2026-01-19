package com.kh.even.back.review.model.service;

import com.kh.even.back.review.model.dto.ReviewDetailDTO;

public interface ReviewService {

    /**
     * 채팅방 기준 리뷰 조회
     * - 없으면 null 또는 빈 DTO 반환 (정책에 맞게)
     */
    ReviewDetailDTO getReview(Long roomNo, Long userNo);

}
