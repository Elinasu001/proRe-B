package com.kh.even.back.admin.model.service;

import com.kh.even.back.admin.model.dto.AdminReportDTO;
import com.kh.even.back.admin.model.dto.AdminReportListResponse;

/**
 * 관리자 신고 관리 서비스 인터페이스
 */
public interface AdminReportService {

    /**
     * 신고 목록 조회 (페이징 + 검색)
     *
     * @param currentPage 현재 페이지 번호
     * @param status 신고 상태 (선택)
     * @param reasonNo 신고 사유 번호 (선택)
     * @return 신고 목록 + 페이징 정보
     */
    AdminReportListResponse getReportListWithPaging(int currentPage, String status, Integer reasonNo);

    /**
     * 신고 상세 조회
     *
     * @param reportNo 신고 번호
     * @return 신고 상세 정보
     */
    AdminReportDTO getReportDetail(Long reportNo);

    /**
     * 신고 대상자별 신고 내역 조회
     *
     * @param targetUserNo 신고 대상 회원 번호
     * @param currentPage 현재 페이지 번호
     * @return 신고 목록 + 페이징 정보
     */
    AdminReportListResponse getReportsByTargetUser(Long targetUserNo, int currentPage);

    /**
     * 신고 상태 변경 + 답변 등록
     *
     * @param reportNo 신고 번호
     * @param status 변경할 상태 (WAITING, RESOLVED, REJECTED)
     * @param answer 답변 내용
     */
    void updateReportStatus(Long reportNo, String status, String answer);

    /**
     * 신고 답변만 수정
     *
     * @param reportNo 신고 번호
     * @param answer 답변 내용
     */
    void updateAnswer(Long reportNo, String answer);
}