package com.kh.even.back.report.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
// import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.even.back.auth.model.vo.CustomUserDetails;
import com.kh.even.back.common.ResponseData;
import com.kh.even.back.report.model.dto.ReportDTO;
import com.kh.even.back.report.model.dto.ReportDetailDTO;
import com.kh.even.back.report.model.dto.ReportTagDTO;
import com.kh.even.back.report.model.service.ReportService;
import com.kh.even.back.report.model.vo.ReportVO;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;
    

    /**
     * 신고 조회
     */
    @GetMapping("/{estimateNo}")
    public ResponseEntity<ResponseData<ReportDetailDTO>> getReport(
            @PathVariable("estimateNo") Long estimateNo,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        //log.info("신고 조회 요청 - estimateNo: {}, userNo: {}", estimateNo, user.getUserNo());
        ReportDetailDTO report = reportService.getReport(
            estimateNo,
            user.getUserNo()
        );
        //log.info("신고 조회 결과: {}", report);
        return ResponseData.ok(report, "신고 조회에 성공했습니다");
    }


    /**
	 * 태그 전체 조회 (등록 시 필요)
	 * 
	 */
    @GetMapping("/tags")
	public ResponseEntity<ResponseData<List<ReportTagDTO>>> getAllReportTags() {
		List<ReportTagDTO> categories = reportService.getAllReportTags();
		return ResponseData.ok(categories, "전체 태그 목록 조회에 성공했습니다");
	}


    /**
	 * 신고 등록
	 */
    @PostMapping
    public ResponseEntity<ResponseData<ReportVO>> saveReport(
            @Valid @ModelAttribute ReportDTO reportDTO,
            @AuthenticationPrincipal CustomUserDetails user
        ) {
        
        //log.info("신고 등록 요청 - estimateNo: {}, userNo: {}, targetUserNo: {}, content: {}", reportDTO.getEstimateNo(), user.getUserNo(), reportDTO.getTargetUserNo(), reportDTO.getContent());
        // 권한 검증 (해당 견적의 의뢰인인지 확인)
        ReportVO saved = reportService.saveReport(reportDTO,
            user.getUserNo()
        );
        return ResponseData.created(saved, "신고가 성공적으로 등록되었습니다");
    }
}
