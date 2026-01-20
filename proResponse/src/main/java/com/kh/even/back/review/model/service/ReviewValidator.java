package com.kh.even.back.review.model.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.kh.even.back.exception.ReviewException;
import com.kh.even.back.review.model.dao.ReviewMapper;
import com.kh.even.back.review.model.dto.ReviewDTO;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ReviewValidator {

    // private static final int MAX_TAG_COUNT = 4;
    //private final ReviewMapper reviewMapper;

	// public void validateCreateReview(ReviewRequestDTO request) {
	
    //     // 1. 중복 리뷰 검증
	//     if (reviewMapper.existsByEstimateNo(request.getEstimateNo())) {
	//         throw new ReviewException("이미 리뷰가 작성되었습니다");
	//     }
	   
	// }

    // private void validateReviewTags(List<Long> tagNos) {

    //     if (tagNos == null || tagNos.isEmpty()) return;

    //     // 태그 개수 제한
    //     if (tagNos.size() > MAX_TAG_COUNT) {
    //         throw new ReviewException(
    //             "태그는 최대 " + MAX_TAG_COUNT + "개까지만 선택 가능합니다"
    //         );
    //     }

    //     // 태그 존재 여부 검증
    //     int validCount = reviewMapper.existingReviewTags(tagNos);

    //     if (validCount != tagNos.size()) {
    //         throw new ReviewException("존재하지 않는 태그가 포함되어 있습니다");
    //     }
    // }

}
