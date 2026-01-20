package com.kh.even.back.admin.model.service;

import java.util.List;
import com.kh.even.back.admin.model.dto.MemberDTO;

/**
 * 관리자 - 회원 관리 서비스
 */
public interface MemberService {
    
    /**
     * 회원 목록 조회 (페이징 + 검색)
     */
    List<MemberDTO> getMemberList(int currentPage, String keyword);
    
    /**
     * 회원 전체 개수 조회 (검색 포함)
     */
    int getMemberCount(String keyword);
    
    /**
     * 회원 상세 정보 조회
     */
    MemberDTO getMemberDetail(Long userNo);
    
    /**
     * 회원 활성화 (비즈니스 의도 명확)
     */
    boolean activateMember(Long userNo);
    
    /**
     * 회원 비활성화 (비즈니스 의도 명확)
     */
    boolean deactivateMember(Long userNo);
    
    /**
     * 회원 권한 변경
     */
    boolean changeMemberRole(Long userNo, String newRole);
    
    /**
     * 징계 적용
     */
    boolean applyPenaltyToMember(Long userNo);
    
    /**
     * 징계 해제
     */
    boolean removePenaltyFromMember(Long userNo);
}