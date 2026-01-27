package com.kh.even.back.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kh.even.back.admin.model.dto.AdminReportDTO;
import com.kh.even.back.admin.model.dto.AdminReportListResponse;
import com.kh.even.back.admin.model.dto.AdminReportSearchRequest;
import com.kh.even.back.admin.model.service.AdminReportService;
import com.kh.even.back.common.ResponseData;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 관리자 신고 관리 컨트롤러
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/api/admin/reports")
@RequiredArgsConstructor
public class AdminReportController {

    private final AdminReportService adminReportService;

    /**
     * 신고 목록 조회 (페이징 + 검색)
     * GET /api/admin/reports?currentPage=1&status=WAITING&reasonNo=1
     */
    @GetMapping
    public ResponseEntity<ResponseData<AdminReportListResponse>> getReportList(
        @Valid @ModelAttribute AdminReportSearchRequest request
    ) {
        log.info("신고 목록 조회 - {}", request);

        AdminReportListResponse response = adminReportService.getReportListWithPaging(
            request.getCurrentPage(),
            request.getStatus(),
            request.getReasonNo()
        );

        return ResponseData.ok(response);
    }

    /**
     * 신고 상세 조회
     * GET /api/admin/reports/{reportNo}
     */
    @GetMapping("/{reportNo}")
    public ResponseEntity<ResponseData<AdminReportDTO>> getReportDetail(
        @PathVariable("reportNo") Long reportNo
    ) {
        log.info("신고 상세 조회 - reportNo: {}", reportNo);

        AdminReportDTO report = adminReportService.getReportDetail(reportNo);
        return ResponseData.ok(report);
    }

    /**
     * 신고 대상자별 신고 내역 조회
     * GET /api/admin/reports/target/{targetUserNo}?currentPage=1
     */
    @GetMapping("/target/{targetUserNo}")
    public ResponseEntity<ResponseData<AdminReportListResponse>> getReportsByTargetUser(
        @PathVariable("targetUserNo") Long targetUserNo,
        @RequestParam(name = "currentPage", defaultValue = "1") int currentPage
    ) {
        log.info("신고 대상자별 내역 조회 - targetUserNo: {}, currentPage: {}", targetUserNo, currentPage);

        AdminReportListResponse response = adminReportService.getReportsByTargetUser(targetUserNo, currentPage);
        return ResponseData.ok(response);
    }

    /**
     * 신고 상태 변경 + 답변 등록
     * PATCH /api/admin/reports/{reportNo}/status?status=RESOLVED&answer=답변내용
     */
    @PatchMapping("/{reportNo}/status")
    public ResponseEntity<ResponseData<String>> updateReportStatus(
        @PathVariable("reportNo") Long reportNo,
        @RequestParam(name = "status") 
        @NotBlank(message = "상태는 필수입니다.") 
        String status,
        @RequestParam(name = "answer") 
        @NotBlank(message = "답변은 필수입니다.") 
        String answer
    ) {
        log.info("신고 상태 변경 - reportNo: {}, status: {}", reportNo, status);

        adminReportService.updateReportStatus(reportNo, status, answer);
        return ResponseData.ok("신고 처리가 완료되었습니다.");
    }

    /**
     * 신고 답변만 수정
     * PATCH /api/admin/reports/{reportNo}/answer?answer=수정된답변
     */
    @PatchMapping("/{reportNo}/answer")
    public ResponseEntity<ResponseData<String>> updateAnswer(
        @PathVariable("reportNo") Long reportNo,
        @RequestParam(name = "answer") 
        @NotBlank(message = "답변은 필수입니다.") 
        String answer
    ) {
        log.info("신고 답변 수정 - reportNo: {}", reportNo);

        adminReportService.updateAnswer(reportNo, answer);
        return ResponseData.ok("답변이 수정되었습니다.");
    }
}