package com.kh.even.back.admin.model.service;

import java.util.List;
import com.kh.even.back.admin.model.dto.AdminMemberDTO;

/**
 * 관리자 - 회원 관리 서비스
 */
public interface AdminMemberService {  // ✅ 기능의 시인성 향상을 위한 클래스명 변경
    
    /**
     * 회원 목록 조회 (페이징 + 검색)
     */
    List<AdminMemberDTO> getMemberList(int currentPage, String keyword);
    
    /**
     * 총 회원 수 조회 (검색 포함)
     */
    int getMemberCount(String keyword);
    
    /**
     * 회원 상세 정보 조회
     */
    AdminMemberDTO getMemberDetail(Long userNo);
    
    /**
     * 회원 상태 변경
     */
    boolean updateMemberStatus(Long userNo, char status);
    
    /**
     * 징계 상태 변경
     */
    boolean updatePenaltyStatus(Long userNo, String penaltyStatus);
    
    /**
     * 회원 권한 변경
     */
    boolean updateUserRole(Long userNo, String userRole);
}