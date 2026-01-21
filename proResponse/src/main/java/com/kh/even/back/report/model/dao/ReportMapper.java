package com.kh.even.back.report.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.kh.even.back.report.model.dto.ReportDetailDTO;
import com.kh.even.back.report.model.dto.ReportTagDTO;


@Mapper
public interface ReportMapper {


    /* 신고 상세 조회 */
    ReportDetailDTO getByEstimateNo(Long estimateNo);

    /**
     * 전체 태그 목록 조회
     */
    List<ReportTagDTO> getAllReportTags();

}
