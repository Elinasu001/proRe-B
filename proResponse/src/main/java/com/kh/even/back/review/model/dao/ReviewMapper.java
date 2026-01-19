package com.kh.even.back.review.model.dao;

import com.kh.even.back.review.model.dto.ReviewDetailDTO;
import com.kh.even.back.review.model.dto.ReviewSearchDTO;

public interface ReviewMapper {
    /**
     * 채팅방 기준 리뷰 조회
     */
    ReviewDetailDTO getByRoom(ReviewSearchDTO dto);
}
