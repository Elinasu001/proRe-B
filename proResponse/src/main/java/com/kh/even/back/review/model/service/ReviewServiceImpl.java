package com.kh.even.back.review.model.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.kh.even.back.exception.ReviewException;
import com.kh.even.back.file.service.S3Service;
import com.kh.even.back.review.model.dao.ReviewMapper;
import com.kh.even.back.review.model.dto.ReviewDTO;
import com.kh.even.back.review.model.dto.ReviewDetailDTO;
import com.kh.even.back.review.model.dto.ReviewTagDTO;
import com.kh.even.back.review.model.vo.ReviewAttachmentVO;
import com.kh.even.back.review.model.vo.ReviewMapVO;
import com.kh.even.back.review.model.vo.ReviewVO;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    
    private final ReviewMapper reviewMapper;
    private final S3Service s3Service;

    @Override
    public ReviewDetailDTO getReview(Long estimateNo
        //, Long userNo
    ) {
        // 1. 리뷰 조회
        ReviewDetailDTO reviewDetailDTO = reviewMapper.getByEstimateNo(estimateNo);
        // 2. 리뷰가 없는 경우 예외 처리
        if (reviewDetailDTO == null) {
            throw new ReviewException("해당 견적에 대한 리뷰가 존재하지 않습니다");
        }
        return reviewDetailDTO;
    }


    // 리뷰 등록하기
    @Override
    @Transactional
    public ReviewVO saveReview(ReviewDTO reviewDTO, List<MultipartFile> files
        //, Long userNo
    ) {


        // 견적 기능 구현 후 주석 해제 필요
        /*

        // 1. 견적 응답 확인
        EstimateResponseVO estimate = estimateMapper.selectEstimateByNo(reviewDTO.getEstimateNo());
        if (estimate == null) {
            throw new ReviewException("존재하지 않는 견적입니다");
        }
        
        // 2. 견적 상태 확인
        if (!"ACCEPTED".equals(estimate.getStatus())) {
            throw new ReviewException("수락된 견적만 리뷰 작성이 가능합니다");
        }
        
        */

        log.info("견적 기능 미구현 상태로 - 견적 검증 생략");

        // 3. 이미 리뷰가 있는 지 확인
        boolean exists = reviewMapper.existsReviewByEstimateNo(reviewDTO.getEstimateNo());
        if (exists) {
            throw new ReviewException("이미 리뷰가 작성된 견적서입니다");
        }

        // 4. 태그 개수 검증
        if(reviewDTO.getTagNos() != null && reviewDTO.getTagNos().size() > 4) {
            throw new ReviewException("태그는 최대 4개까지 선택 가능합니다");
        }

        // 리뷰 등록
        ReviewVO reviewVO = ReviewVO.builder()
                .estimateNo(reviewDTO.getEstimateNo())
                .content(reviewDTO.getContent())
                .starScore(reviewDTO.getStarScore())
                .status("Y")
                .build();
        reviewMapper.saveReview(reviewVO);
        Long reviewNo = reviewVO.getReviewNo();
        
         // 태그 매핑 등록
        if (reviewDTO.getTagNos() != null && !reviewDTO.getTagNos().isEmpty()) {
            for (Long tagNo : reviewDTO.getTagNos()) {
                ReviewMapVO mapVO = ReviewMapVO.builder()
                        .reviewNo(reviewNo)
                        .tagNo(tagNo)
                        .build();
                
                reviewMapper.saveReviewMap(mapVO);
            }
            
            //log.info("태그 {}개 등록 완료 - reviewNo: {}", reviewDTO.getTagNos().size(), reviewNo);
        }
        
        // 첨부파일 등록
        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {

                String filePath = s3Service.store(file, "reviews");
                
                ReviewAttachmentVO attachmentVO = ReviewAttachmentVO.builder()
                        .reviewNo(reviewNo)
                        .originName(file.getOriginalFilename())
                        .filePath(filePath)
                        .status("Y")
                        .build();
                
                reviewMapper.saveReviewAttachment(attachmentVO);
            }
        }

        //log.info("리뷰 등록 완료 - reviewNo: {}, estimateNo: {}, 태그수: {}", reviewNo, reviewDTO.getEstimateNo(), reviewDTO.getTagNos() != null ? reviewDTO.getTagNos().size() : 0);
        
        return reviewVO;
    }

    // 태그 목록 조회
	@Override
	public List<ReviewTagDTO> getAllReviewTags() {
		return reviewMapper.getAllReviewTags();
	}
    

    // 리뷰 삭제
    @Override
    public ReviewVO deleteByEstimateNo(Long estimateNo) {
        ReviewVO reviewVO = reviewMapper.getReviewByEstimateNo(estimateNo);
        if (reviewVO == null) {
            throw new ReviewException("존재하지 않는 리뷰입니다");
        }
        // 상태 변경만 수행
        int updated = reviewMapper.updateReviewStatus(reviewVO.getReviewNo());

        if (updated == 0) {
            throw new ReviewException("리뷰 삭제에 실패했습니다.");
        }
        return reviewVO;
    }


}
