package com.kh.even.back.report.model.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.kh.even.back.report.model.dto.ReportDetailDTO;
import com.kh.even.back.report.model.dto.ReportTagDTO;
import com.kh.even.back.review.model.dto.ReviewDTO;
import com.kh.even.back.review.model.vo.ReviewVO;

public interface ReportService {
    
  
    /**
     * 신고 조회
     */
    ReportDetailDTO getReport(Long estimateNo /*, Long userNo */);

    /**
	 *  태그 전체 조회 (등록 시 필요)
	 */
	List<ReportTagDTO> getAllReportTags();

    /**
     * 신고 등록
     * 
     */
    ReviewVO saveReview(ReviewDTO reviewDTO
        //, Long userNo
    );
    
}
