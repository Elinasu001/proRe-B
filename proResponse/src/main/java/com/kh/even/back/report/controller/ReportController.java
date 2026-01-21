package com.kh.even.back.report.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.even.back.auth.model.vo.CustomUserDetails;
import com.kh.even.back.common.ResponseData;
import com.kh.even.back.report.model.service.ReportService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;
    
    /**
     * 신고 여부 조회
     */

    /* @GetMapping("/{roomNo}/status")
=======
    @GetMapping("/{roomNo}")
>>>>>>> e1a430e22cf2bda4c90d8303b8dc6f0144dd4c55
    public  ResponseEntity<ResponseData<Boolean>> getReportStatus(
            @PathVariable Long roomNo
            //, @AuthenticationPrincipal CustomUserDetails user
            ) {
        boolean canReport = reportService.getReportStatus(roomNo
        		//, user.getUserNo()
        		);
        return ResponseData.ok(canReport, "신고 가능 여부 조회 성공");
    }
*/
}
