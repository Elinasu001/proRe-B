package com.kh.even.back.review.model.service;

import java.util.List;

import com.kh.even.back.review.model.dto.ReviewDetailDTO;
import com.kh.even.back.review.model.dto.ReviewDTO;
import com.kh.even.back.review.model.dto.ReviewTagDTO;
import com.kh.even.back.review.model.vo.ReviewVO;
import org.springframework.web.multipart.MultipartFile;

public interface ReviewService {

    /**
     * 채팅방 기준 리뷰 조회
     */
    ReviewDetailDTO getReview(Long estimateNo
        //, Long userNo
    );

    /**
	 * 카테고리 목록 조회
	 * @return 카테고리 목록
	 */
	List<ReviewTagDTO> getAllReviewTags();


    ReviewVO saveReview(ReviewDTO reviewDTO, List<MultipartFile> files
        //, Long userNo
    );

    ReviewVO deleteByEstimateNo(Long estimateNo
        //, Long userNo
    );
}
