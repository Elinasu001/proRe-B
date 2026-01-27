package com.kh.even.back.admin.model.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kh.even.back.admin.model.entity.Report;

public interface AdminReportRepository extends JpaRepository<Report, Long> {

    /**
     * 상태별 조회 (Fetch Join으로 N+1 방지)
     */
    @Query("SELECT r FROM Report r LEFT JOIN FETCH r.reportTag WHERE r.status = :status")
    Page<Report> findByStatusWithTag(@Param("status") String status, Pageable pageable);

    /**
     * 신고 사유별 조회
     */
    @Query("SELECT r FROM Report r LEFT JOIN FETCH r.reportTag WHERE r.reasonNo = :reasonNo")
    Page<Report> findByReasonNoWithTag(@Param("reasonNo") Integer reasonNo, Pageable pageable);

    /**
     * 전체 조회 (Fetch Join)
     */
    @Query("SELECT r FROM Report r LEFT JOIN FETCH r.reportTag")
    Page<Report> findAllWithTag(Pageable pageable);

    /**
     * 복합 검색 (상태 + 신고 사유)
     */
    @Query("SELECT r FROM Report r LEFT JOIN FETCH r.reportTag WHERE " +
           "(:status IS NULL OR r.status = :status) AND " +
           "(:reasonNo IS NULL OR r.reasonNo = :reasonNo)")
    Page<Report> searchReports(
        @Param("status") String status,
        @Param("reasonNo") Integer reasonNo,
        Pageable pageable
    );

    /**
     * 신고 대상자별 조회
     */
    @Query("SELECT r FROM Report r LEFT JOIN FETCH r.reportTag WHERE r.targetUserNo = :targetUserNo")
    Page<Report> findByTargetUserNoWithTag(@Param("targetUserNo") Long targetUserNo, Pageable pageable);

    /**
     * 신고자별 조회
     */
    @Query("SELECT r FROM Report r LEFT JOIN FETCH r.reportTag WHERE r.reporterUserNo = :reporterUserNo")
    Page<Report> findByReporterUserNoWithTag(@Param("reporterUserNo") Long reporterUserNo, Pageable pageable);
    
    /**
     * 신고 상세 조회 (Fetch Join)
     */
    @Query("SELECT r FROM Report r LEFT JOIN FETCH r.reportTag WHERE r.reportNo = :reportNo")
    Optional<Report> findByIdWithTag(@Param("reportNo") Long reportNo);
}