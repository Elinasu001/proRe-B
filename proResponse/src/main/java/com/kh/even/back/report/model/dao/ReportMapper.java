package com.kh.even.back.report.model.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.kh.even.back.report.model.dto.ReportDetailDTO;
import com.kh.even.back.report.model.dto.ReportTagDTO;
import com.kh.even.back.report.model.vo.ReportVO;


@Mapper
public interface ReportMapper {

     // === 신고 조회 ===

    /**
     * 신고 상세 조회
     * @param estimateNo 견적 번호
     * @return 신고 상세 DTO(ReportDetailDTO)
     */
    ReportDetailDTO getByEstimateNo(Map<String, Object> params);

    /**
     * 전체 태그 목록 조회
     * @return 태그 DTO 리스트(List<ReportTagDTO>)
     */
    List<ReportTagDTO> getAllReportTags();

    /**
     * 견적서에 대한 신고 존재 여부 확인
     * @param estimateNo 견적 번호
     * @return 존재 여부(boolean)
     */
    //boolean existsReportByEstimateNo(Map<String, Object> params);

    // 신고자(userNo)와 대상(targetUserNo) 기준 중복 체크
    boolean existsReportByUserAndTarget(Map<String, Object> params);
    
    // === 신고 등록 ===

    /**
     * 신고 등록
     * @param reportVO 신고 VO
     * @return 등록된 행 수(int)
     */
    int saveReport(ReportVO reportVO);


}
