package com.kh.even.back.admin.model.service;

import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AdminReportValidator {

    /**
     * 신고 상태 변경 검증
     */
    public void validateStatusChange(String currentStatus, String newStatus) {
        if (newStatus == null || newStatus.isEmpty()) {
            throw new IllegalArgumentException("상태 값은 필수입니다.");
        }

        if (!isValidStatus(newStatus)) {
            throw new IllegalArgumentException("유효하지 않은 상태입니다. (WAITING, RESOLVED, REJECTED만 가능)");
        }

        // 이미 처리 완료된 신고는 수정 불가
        if ("RESOLVED".equals(currentStatus) || "REJECTED".equals(currentStatus)) {
            throw new IllegalStateException("이미 처리 완료된 신고는 수정할 수 없습니다.");
        }

        log.debug("신고 상태 변경 검증 완료: {} -> {}", currentStatus, newStatus);
    }

    /**
     * 신고 답변 검증
     */
    public void validateAnswer(String answer) {
        if (answer == null || answer.trim().isEmpty()) {
            throw new IllegalArgumentException("답변 내용은 필수입니다.");
        }

        if (answer.length() > 2000) {
            throw new IllegalArgumentException("답변은 2000자를 초과할 수 없습니다.");
        }

        log.debug("신고 답변 검증 완료");
    }

    /**
     * 신고 사유 번호 검증
     */
    public void validateReasonNo(Integer reasonNo) {
        if (reasonNo == null) {
            throw new IllegalArgumentException("신고 사유 번호는 필수입니다.");
        }

        if (reasonNo <= 0) {
            throw new IllegalArgumentException("유효하지 않은 신고 사유 번호입니다.");
        }

        log.debug("신고 사유 번호 검증 완료: {}", reasonNo);
    }

    /**
     * 상태 + 답변 동시 검증
     */
    public void validateStatusWithAnswer(String newStatus, String answer) {
        validateStatusChange(null, newStatus);

        // RESOLVED나 REJECTED로 변경 시 답변 필수
        if ("RESOLVED".equals(newStatus) || "REJECTED".equals(newStatus)) {
            validateAnswer(answer);
        }

        log.debug("상태 및 답변 검증 완료");
    }

    /**
     * 유효한 상태인지 확인
     */
    private boolean isValidStatus(String status) {
        return status.equals("WAITING") ||
               status.equals("RESOLVED") ||
               status.equals("REJECTED");
    }
}