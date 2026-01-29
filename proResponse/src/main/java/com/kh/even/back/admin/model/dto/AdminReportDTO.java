package com.kh.even.back.admin.model.dto;

import java.time.LocalDate;
import lombok.*;

/**
 * 관리자 신고 상세 조회 DTO
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class AdminReportDTO {
    
    private Long reportNo;
    private String content;
    private LocalDate createDate;
    private LocalDate updateDate;
    private String status;
    private Long reporterUserNo;
    private Long targetUserNo;
    private Integer reasonNo;
    private Long estimateNo;
    private String answer;
    private LocalDate answerDate;
    
    // 추가 정보 (JOIN 결과)
    private String reasonName;           // 신고 사유명
    private String reporterNickname;     // 신고자 닉네임 (선택)
    private String targetNickname;       // 신고 대상 닉네임 (선택)
    
    /**
     * Entity → DTO 변환
     */
    public static AdminReportDTO fromEntity(com.kh.even.back.admin.model.entity.Report report) {
        return AdminReportDTO.builder()
            .reportNo(report.getReportNo())
            .content(report.getContent())
            .createDate(report.getCreateDate())
            .updateDate(report.getUpdateDate())
            .status(report.getStatus())
            .reporterUserNo(report.getReporterUserNo())
            .targetUserNo(report.getTargetUserNo())
            .reasonNo(report.getReasonNo())
            .estimateNo(report.getEstimateNo())
            .answer(report.getAnswer())
            .answerDate(report.getAnswerDate())
            .reasonName(report.getReportTag() != null ? report.getReportTag().getReasonName() : null)
            .build();
    }
}