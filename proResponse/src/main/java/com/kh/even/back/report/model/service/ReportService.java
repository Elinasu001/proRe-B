package com.kh.even.back.report.model.service;

public interface ReportService {
    
    /**
     * 신고 가능 여부 조회
     * @return true  : 신고 가능
     *         false : 이미 신고함
     */
    boolean getReportStatus(Long roomNo, Long userNo);

}
