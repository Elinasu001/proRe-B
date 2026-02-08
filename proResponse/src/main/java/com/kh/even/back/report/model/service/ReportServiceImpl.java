package com.kh.even.back.report.model.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.kh.even.back.exception.ReportException;
import com.kh.even.back.report.model.dao.ReportMapper;
import com.kh.even.back.report.model.dto.ReportDTO;
import com.kh.even.back.report.model.dto.ReportDetailDTO;
import com.kh.even.back.report.model.dto.ReportTagDTO;
import com.kh.even.back.report.model.vo.ReportVO;

import jakarta.transaction.Transactional;
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
    public ReportDetailDTO getReport(Long estimateNo , Long userNo) {
    // estimateNo, userNo를 하나의 params(Map)로 묶어서 전달

    Map<String, Object> params = Map.of("estimateNo", estimateNo, "userNo", userNo);
    ReportDetailDTO reportDetailDTO = reportMapper.getByEstimateNo(params);
    // if(reportDetailDTO == null){
    //     throw new ReportException("해당 견적에 대한 신고가 존재하지 않습니다");
    // }
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

        // 1. 이미 신고가 되어 있는 지 확인 (신고자+대상 기준)
        validateNotReported(reportDTO.getEstimateNo(), userNo, reportDTO.getTargetUserNo());

        // 1-1. reasonNo null 체크
        if (reportDTO.getReasonNo() == null) {
            throw new ReportException("신고 사유(REASON_NO)는 필수입니다.");
        }

        // 2. 신고 등록
        ReportVO reportVO = ReportVO.builder()
            .estimateNo(reportDTO.getEstimateNo())
            .content(reportDTO.getContent())
            .userNo(userNo)
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


    public void validateNotReported(Long estimateNo, Long userNo, Long targetUserNo) {
        //log.debug("[validateNotReported] userNo={}, targetUserNo={}", userNo, targetUserNo);
        if (estimateNo == null || userNo == null || targetUserNo == null) {
            //log.warn("[validateNotReported] userNo 또는 targetUserNo가 null입니다. userNo={}, targetUserNo={}", userNo, targetUserNo);
            throw new ReportException("userNo, targetUserNo는 null일 수 없습니다.");
        }
        boolean exists = reportMapper.existsReportByUserAndTarget(
            Map.of("estimateNo", estimateNo,"userNo", userNo, "targetUserNo", targetUserNo)
        );
        //log.debug("[validateNotReported] existsReportByUserAndTarget result={}", exists);
        if (exists) {
            //log.warn("[validateNotReported] 이미 신고가 접수된 사용자입니다. userNo={}, targetUserNo={}", userNo, targetUserNo);
            throw new ReportException("이미 신고가 접수된 사용자입니다.");
        }
    }

}
