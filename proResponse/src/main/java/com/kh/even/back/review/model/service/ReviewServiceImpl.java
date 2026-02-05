
package com.kh.even.back.review.model.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.kh.even.back.common.validator.AssertUtil;
import com.kh.even.back.exception.ReviewException;
import com.kh.even.back.file.service.FileUploadService;
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
    //private final S3Service s3Service;
    private final Pagenation pagenation;
    private final FileUploadService fileUploadService;

    // 리뷰 조회 (작성 후 상세 조회)
    @Override
    public ReviewDetailDTO getReview(Long estimateNo,  Long userNo) {

        assertRoleUser(userNo, "리뷰를 조회");

        ReviewDetailDTO reviewDetailDTO = reviewMapper.getByEstimateNo(estimateNo);
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
    public ReviewVO saveReview(ReviewDTO reviewDTO, List<MultipartFile> files, Long userNo) {
        
        assertRoleUser(userNo, "리뷰를 작성");

        // 1. 내가 이미 쓴 리뷰가 있는지 확인
        boolean exists = existsByEstimateNo(reviewDTO.getEstimateNo(), userNo);
        
        if (exists) {
            throw new ReviewException("이미 리뷰가 작성된 견적서입니다");
        }

        // 2. 리뷰 등록
        ReviewVO reviewVO = ReviewVO.builder()
                .estimateNo(reviewDTO.getEstimateNo())
                .content(reviewDTO.getContent())
                .starScore(reviewDTO.getStarScore())
                .status("Y")
                .build();

        int reviewResult = reviewMapper.saveReview(reviewVO);
        ReviewValidator.validateDbResult(reviewResult, "리뷰 등록에 실패했습니다.");

        Long reviewNo = reviewVO.getReviewNo();

        // 3. 태그 매핑 등록
        if (reviewDTO.getTagNos() != null && !reviewDTO.getTagNos().isEmpty()) {
            for (Long tagNo : reviewDTO.getTagNos()) {
                ReviewMapVO mapVO = ReviewMapVO.builder()
                        .reviewNo(reviewNo)
                        .tagNo(tagNo)
                        .build();
                int mapResult = reviewMapper.saveReviewMap(mapVO);
                ReviewValidator.validateDbResult(mapResult, "리뷰 태그 매핑에 실패했습니다.");
            }
        }

        // 4. 첨부파일 등록 (files 필드 사용)
        // List<MultipartFile> attachments = reviewDTO.getFiles();
        // if (attachments != null && !attachments.isEmpty()) {
        //     for (MultipartFile file : attachments) {
        //         String filePath = s3Service.store(file, "reviews");
        //         ReviewAttachmentVO attachmentVO = ReviewAttachmentVO.builder()
        //                 .reviewNo(reviewNo)
        //                 .originName(file.getOriginalFilename())
        //                 .filePath(filePath)
        //                 .uploadDate(LocalDateTime.now())
        //                 .status("Y")
        //                 .build();
        //         int attachResult = reviewMapper.saveReviewAttachment(attachmentVO);
        //         ReviewValidator.validateDbResult(attachResult, "리뷰 첨부파일 저장에 실패했습니다.");
        //     }
        // }

        saveAttachments(files, reviewNo);

        //log.info("리뷰 등록 완료 - reviewNo: {}, estimateNo: {}, 태그수: {}", reviewNo, reviewDTO.getEstimateNo(), reviewDTO.getTagNos() != null ? reviewDTO.getTagNos().size() : 0);
        return reviewVO;
    }


    private void saveAttachments(List<MultipartFile> files, Long reviewNo) {
        AssertUtil.validateImageFiles(files);
            fileUploadService.uploadFiles(
                files,
                "reviews",
                reviewNo,
                fileVO -> {

                    ReviewAttachmentVO attachmentVO =
                        ReviewAttachmentVO.builder()
                            .reviewNo(reviewNo)
                            .originName(fileVO.getOriginName())
                            .filePath(fileVO.getFilePath())
                            .status("Y")
                            .build();

                    reviewMapper.saveReviewAttachment(attachmentVO);
                }
            );
    }


    
    // 회원 권한 체크 공통 메서드
    private void assertRoleUser(Long userNo, String action) {
        String userRole = reviewMapper.getUserRoleByUserNo(userNo);
        if (!"ROLE_USER".equals(userRole)) {
            throw new ReviewException("일반회원만 " + action + "할 수 있습니다.");
        }
    }


    /**
    * 견적번호+회원번호로 리뷰 존재 여부 확인
     */
    public boolean existsByEstimateNo(Long estimateNo, Long userNo) {
        Map<String, Object> params = Map.of(
            "estimateNo", estimateNo,
            "userNo", userNo
        );
        return reviewMapper.existsByEstimateNo(params);
    }
    

    // 리뷰 삭제
    @Override
    @Transactional
    public ReviewVO deleteByEstimateNo(Long estimateNo, Long userNo) {
        ReviewVO reviewVO = reviewMapper.getReviewByEstimateNo(estimateNo);
        if (reviewVO == null) {
            throw new ReviewException("존재하지 않는 리뷰입니다");
        }
        // 상태 변경만 수행
        Long reviewNo = reviewVO.getReviewNo();
        int mapDeleted = reviewMapper.deleteReviewMapByReviewNo(reviewNo);      // 1. 태그 매핑 삭제
        int attachDeleted = reviewMapper.deleteAttachmentsByReviewNo(reviewNo); // 2. 첨부파일 삭제
        int deleted = reviewMapper.deleteReviewByReviewNo(reviewNo);            // 3. 리뷰 삭제
        if(mapDeleted == 0 && attachDeleted == 0 && deleted == 0) {
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
		
		List<ExpertReviewVO> vo = new ArrayList<>();
		
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
