package com.kh.even.back.report.model.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kh.even.back.exception.ReportException;
import com.kh.even.back.report.model.dao.ReportMapper;
import com.kh.even.back.report.model.dto.ReportDTO;
import com.kh.even.back.report.model.dto.ReportDetailDTO;
import com.kh.even.back.report.model.dto.ReportTagDTO;
import com.kh.even.back.report.model.vo.ReportVO;
import com.kh.even.back.review.model.service.ReviewService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ReportMapper reportMapper;
    private final ReviewService reviewService;

    /**
     * 신고 조회
     */
    @Override
    public ReportDetailDTO getReport(Long estimateNo , Long userNo) {

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

    /**
     * 신고 등록
     */
    @Override
    @Transactional
    public ReportVO saveReport(ReportDTO reportDTO, Long userNo) {

        // 견적 기능 구현 후 주석 해제 필요
        /*

        // 1. 견적 응답 확인
        EstimateResponseVO estimate = estimateMapper.selectEstimateByNo(reportDTO.getEstimateNo());
        if (estimate == null) {
            throw new ReportException("존재하지 않는 견적입니다");
        }
        
        // 2. 견적 상태 확인
        if (!"ACCEPTED".equals(estimate.getStatus())) {
            throw new ReportException("수락된 견적만 신고 작성이 가능합니다");
        }
        
        */

        //log.info("견적 기능 미구현 상태로 - 견적 검증 생략");

        // 3. 이미 신고가 되어 있는 지 확인
        boolean exists = reviewService.existsByEstimateNo(reportDTO.getEstimateNo());
        if (exists) {
            throw new ReportException("이미 신고가 작성된 견적서입니다");
        }

        // 4. 신고 등록
        ReportVO reportVO = ReportVO.builder()
            .estimateNo(reportDTO.getEstimateNo())
            .content(reportDTO.getContent())
            .createDate(reportDTO.getCreateDate())
            .updateDate(reportDTO.getUpdateDate())
            .status("WAITING")
            .reasonNo(reportDTO.getReasonNo())
            .estimateNo(reportDTO.getEstimateNo())
            .reporterUserNo(userNo)
            .targetUserNo(reportDTO.getTargetUserNo())
            .build();

        int reportResult = reportMapper.saveReport(reportVO);
        ReportValidator.validateDbResult(reportResult, "신고 등록에 실패했습니다.");

        return reportVO;
    }

}
