package com.kh.even.back.report.model.service;

import java.util.List;

import com.kh.even.back.report.model.dto.ReportDTO;
import com.kh.even.back.report.model.dto.ReportDetailDTO;
import com.kh.even.back.report.model.dto.ReportTagDTO;
import com.kh.even.back.report.model.vo.ReportVO;


public interface ReportService {
    
    // === 조회 ===

    /**
     * 신고 상세 조회
     * @param estimateNo 견적 번호
     * @return 신고 상세 DTO(ReportDetailDTO)
     */
    ReportDetailDTO getReport(Long estimateNo, Long userNo);

    /**
     * 태그 전체 조회 (등록 시 필요)
     * @return 태그 DTO 리스트(List<ReportTagDTO>)
     */
    List<ReportTagDTO> getAllReportTags();


    // === 등록 === 

    /**
     * 신고 등록
     * @param reportDTO 신고 DTO
     * @return 등록된 신고 VO(ReportVO)
     */
    ReportVO saveReport(ReportDTO reportDTO, Long userNo);


}
