package com.kh.even.back.admin.model.dto;

import jakarta.validation.constraints.Min;
import lombok.*;

/**
 * 관리자 신고 검색 요청 DTO
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class AdminReportSearchRequest {
    
    @Min(value = 1, message = "페이지 번호는 1 이상이어야 합니다.")
    private int currentPage = 1;
    
    private String status;           // 신고 상태 (WAITING, RESOLVED, REJECTED)
    private Integer reasonNo;        // 신고 사유 번호
    private Long targetUserNo;       // 신고 대상 회원 번호
    private Long reporterUserNo;     // 신고자 회원 번호
}