package com.kh.even.back.admin.model.service;

import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AdminMemberStatusManager {
    
    /**
     * 회원 활성화 판단
     */
    public String activateMember() {
        log.debug("회원 활성화 상태로 변경");
        return "Y";
    }
    
    /**
     * 회원 비활성화 판단
     */
    public String deactivateMember() {
        log.debug("회원 비활성화 상태로 변경");
        return "N";
    }
    
    /**
     * 징계 적용
     */
    public String applyPenalty() {
        log.debug("징계 상태 적용");
        return "Y";
    }
    
    /**
     * 징계 해제
     */
    public String removePenalty() {
        log.debug("징계 상태 해제");
        return "N";
    }
    
    /**
     * 상태 변환 (char -> String)
     */
    public char toStatusChar(String status) {
        return status != null && !status.isEmpty() ? status.charAt(0) : 'Y';
    }
}