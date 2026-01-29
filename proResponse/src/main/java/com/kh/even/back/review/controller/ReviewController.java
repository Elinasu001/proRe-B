package com.kh.even.back.review.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kh.even.back.auth.model.vo.CustomUserDetails;
import com.kh.even.back.common.ResponseData;
import com.kh.even.back.review.model.dto.ReviewDTO;
import com.kh.even.back.review.model.dto.ReviewDetailDTO;
import com.kh.even.back.review.model.dto.ReviewTagDTO;
import com.kh.even.back.review.model.service.ReviewService;
import com.kh.even.back.review.model.vo.ExpertReviewVO;
import com.kh.even.back.review.model.vo.ReviewVO;
import com.kh.even.back.util.model.dto.PageResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    
    /**
    * 리뷰 조회
    */
    @GetMapping("/{estimateNo}")
    public ResponseEntity<ResponseData<ReviewDetailDTO>> getReview(
            @PathVariable("estimateNo") Long estimateNo,
            @AuthenticationPrincipal CustomUserDetails user
        ) {
        ReviewDetailDTO review = reviewService.getReview(
            estimateNo,
            user.getUserNo()
        );
        return ResponseData.ok(review, "리뷰 조회에 성공했습니다");
    }

    /**
	 * 태그 전체 조회 (등록 시 필요)
	 * 
	 */
    @GetMapping("/tags")
	public ResponseEntity<ResponseData<List<ReviewTagDTO>>> getAllReviewTags() {
		List<ReviewTagDTO> categories = reviewService.getAllReviewTags();
		return ResponseData.ok(categories, "전체 태그 목록 조회에 성공했습니다");
	}

    /**
     * 리뷰 등록
     */
    @PostMapping
    public ResponseEntity<ResponseData<ReviewVO>> saveReview(
            @Valid ReviewDTO reviewDTO,
            @RequestParam(value = "files", required = false) List<MultipartFile> files,
            @AuthenticationPrincipal CustomUserDetails user
        ) {
        
        //log.info("리뷰 등록 요청 - estimateNo: {}, userNo: {}, starScore: {}", reviewDTO.getEstimateNo(), user.getUserNo(), reviewDTO.getStarScore());
        
        // 권한 검증 (해당 견적의 의뢰인인지 확인)
        ReviewVO saved = reviewService.saveReview(
            reviewDTO
            , files
            , user.getUserNo()
        );
        
        return ResponseData.created(saved, "리뷰가 성공적으로 등록되었습니다");
    }

    /**
	 * 리뷰 삭제
	 * 
	 */
    @PutMapping("/{estimateNo}")
    public ResponseEntity<ResponseData<ReviewVO>> deleteByEstimateNo(
        @PathVariable("estimateNo") Long estimateNo,
        @AuthenticationPrincipal CustomUserDetails user
    ) {
        ReviewVO deleted = reviewService.deleteByEstimateNo(
            estimateNo,
            user.getUserNo()
        );
        return ResponseData.created(deleted, "리뷰가 삭제되었습니다");
    }
    
    @GetMapping("/expert/{expertNo}")
    public ResponseEntity<ResponseData<PageResponse<ExpertReviewVO>>> getExpertReviews(
        @PathVariable(name="expertNo") Long expertNo,
        @RequestParam(name="pageNo" , defaultValue = "1") int pageNo){
    
    	return ResponseData.ok(reviewService.getExpertReviews(expertNo,pageNo),"조회에 성공했습니다.");
    	
    }
    
}
