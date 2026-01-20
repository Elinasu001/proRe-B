package com.kh.even.back.admin.model.service;

import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MemberValidator {
    
    /**
     * 상태 변경 검증
     */
    public void validateStatusChange(String currentStatus, String newStatus) {
        if (newStatus == null || newStatus.isEmpty()) {
            throw new IllegalArgumentException("상태 값은 필수입니다.");
        }
        
        if (!newStatus.equals("Y") && !newStatus.equals("N")) {
            throw new IllegalArgumentException("상태는 Y 또는 N만 가능합니다.");
        }
        
        log.debug("상태 변경 검증 완료: {} -> {}", currentStatus, newStatus);
    }
    
    /**
     * 권한 변경 검증
     */
    public void validateRoleChange(String currentRole, String newRole) {
        if (newRole == null || newRole.isEmpty()) {
            throw new IllegalArgumentException("권한 값은 필수입니다.");
        }
        
        if (!isValidRole(newRole)) {
            throw new IllegalArgumentException("유효하지 않은 권한입니다.");
        }
        
        log.debug("권한 변경 검증 완료: {} -> {}", currentRole, newRole);
    }
    
    /**
     * 징계 상태 변경 검증
     */
    public void validatePenaltyChange(String currentPenalty, String newPenalty) {
        if (newPenalty == null || newPenalty.isEmpty()) {
            throw new IllegalArgumentException("징계 상태 값은 필수입니다.");
        }
        
        if (!newPenalty.equals("Y") && !newPenalty.equals("N")) {
            throw new IllegalArgumentException("징계 상태는 Y 또는 N만 가능합니다.");
        }
        
        log.debug("징계 상태 변경 검증 완료: {} -> {}", currentPenalty, newPenalty);
    }
    
    /**
     * 유효한 권한인지 확인
     */
    private boolean isValidRole(String role) {
        return role.equals("ROLE_USER") || 
               role.equals("ROLE_EXPERT") || 
               role.equals("ROLE_ADMIN");
    }
}
