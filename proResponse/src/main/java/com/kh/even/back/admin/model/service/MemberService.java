package com.kh.even.back.admin.model.service;

import java.util.List;
import com.kh.even.back.admin.model.dto.MemberDTO;
import com.kh.even.back.member.model.vo.MemberVO;

public interface MemberService {
    
    /**
     * 회원 목록 조회 (페이징 + 검색)
     */
    List<MemberDTO> getMemberList(int currentPage, String keyword);
    
    /**
     * 회원 전체 개수 (검색 포함)
     */
    int getMemberCount(String keyword);
    
    /**
     * 회원 상세 조회
     */
    MemberDTO getMemberDetail(Long userNo);
    
    /**
     * 회원 상태 변경 (활성/탈퇴)
     */
    boolean updateMemberStatus(Long userNo, char status);
    
    /**
     * 권한 변경 (일반/전문가/관리자)
     */
    boolean updateUserRole(Long userNo, String userRole);
    
    /**
     * 징계 상태 변경
     */
    boolean updatePenaltyStatus(Long userNo, char penaltyStatus);
}