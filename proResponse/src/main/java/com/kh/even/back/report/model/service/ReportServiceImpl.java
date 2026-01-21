package com.kh.even.back.report.model.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kh.even.back.exception.ReportException;
import com.kh.even.back.report.model.dao.ReportMapper;
import com.kh.even.back.report.model.dto.ReportDetailDTO;
import com.kh.even.back.report.model.dto.ReportTagDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ReportMapper reportMapper;

    /**
     * 신고 조회
     */
    @Override
    public ReportDetailDTO getReport(Long estimateNo /*, Long userNo */) {

        ReportDetailDTO reportDetailDTO = reportMapper.getByEstimateNo(estimateNo);

        if(reportDetailDTO == null){
            throw new ReportException("해당 견적에 대한 신고가 존재하지 않습니다");
        }
        return reportDetailDTO;
    }

    // 태그 목록 전체 조회 (등록 시 필요)
	@Override
	public List<ReportTagDTO> getAllReportTags() {
		return reportMapper.getAllReportTags();
	}


 

}
