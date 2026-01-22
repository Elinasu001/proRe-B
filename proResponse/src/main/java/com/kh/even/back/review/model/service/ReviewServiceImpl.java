package com.kh.even.back.review.model.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.kh.even.back.common.validator.AssertUtil;
import com.kh.even.back.exception.ReviewException;
import com.kh.even.back.file.service.S3Service;
import com.kh.even.back.review.model.dao.ReviewMapper;
import com.kh.even.back.review.model.dto.ExpertReviewDTO;
import com.kh.even.back.review.model.dto.ReviewDTO;
import com.kh.even.back.review.model.dto.ReviewDetailDTO;
import com.kh.even.back.review.model.dto.ReviewTagDTO;
import com.kh.even.back.review.model.vo.ExpertReviewVO;
import com.kh.even.back.review.model.vo.ReviewAttachmentVO;
import com.kh.even.back.review.model.vo.ReviewMapVO;
import com.kh.even.back.review.model.vo.ReviewVO;
import com.kh.even.back.util.PageInfo;
import com.kh.even.back.util.Pagenation;
import com.kh.even.back.util.model.dto.PageResponse;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    
    private final ReviewMapper reviewMapper;
    private final S3Service s3Service;
    private final Pagenation pagenation;



    // 리뷰 조회 (작성 후 상세 조회)
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

    // 태그 목록 전체 조회 (등록 시 필요)
	@Override
	public List<ReviewTagDTO> getAllReviewTags() {
		return reviewMapper.getAllReviewTags();
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

        //log.info("견적 기능 미구현 상태로 - 견적 검증 생략");

        // 3. 이미 리뷰가 있는 지 확인
        boolean exists = existsByEstimateNo(reviewDTO.getEstimateNo());
        if (exists) {
            throw new ReviewException("이미 리뷰가 작성된 견적서입니다");
        }

        // 4. 리뷰 등록
        ReviewVO reviewVO = ReviewVO.builder()
                .estimateNo(reviewDTO.getEstimateNo())
                .content(reviewDTO.getContent())
                .starScore(reviewDTO.getStarScore())
                .status("Y")
                .build();

        reviewMapper.saveReview(reviewVO);

        Long reviewNo = reviewVO.getReviewNo();
         // 5. 태그 매핑 등록
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
        
        // 6. AssertUtil.checkFilesSize(files); - pull 받고 다시 적용 필요

        // 7. 첨부파일 등록
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


    /**
    * 견적 번호로 리뷰 존재 여부 확인
     */
    @Override
    public boolean existsByEstimateNo(Long estimateNo) {
        return reviewMapper.existsByEstimateNo(estimateNo);
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

	@Override
	public PageResponse<ExpertReviewVO> getExpertReviews(Long expertNo , int pageNo) {
		
		int listCount = reviewMapper.getReviewsCountByExpertNo(expertNo);
		
		AssertUtil.notFound(listCount, "리뷰 조회 결과가 없습니다.");
		
		Map<String,Object> params = pagenation.pageRequest(pageNo, 5, listCount);
		
		List<ExpertReviewDTO> list = reviewMapper.getExpertReviews(expertNo);
		
		List<ExpertReviewVO> vo = new ArrayList();
		
		list.forEach(
				dto -> {
					vo.add(ExpertReviewVO.builder().nickname(dto.getNickname())
							                       .tagNames(dto.getTagNames().split(","))
							                       .categoryName(dto.getCategoryName())
							                       .starScore(dto.getStarScore())
							                       .content(dto.getContent())
							                       .createDate(dto.getCreateDate())
							                       .filePaths(dto.getFilePaths())
							                       .build());
				}
				);
		
		
		// log.info(" ExpertReviewDTO list : {} " , list );
		
		PageInfo pageInfo = (PageInfo) params.get("pi");
		
		return new PageResponse<ExpertReviewVO>(vo,pageInfo);
		
	}


}
