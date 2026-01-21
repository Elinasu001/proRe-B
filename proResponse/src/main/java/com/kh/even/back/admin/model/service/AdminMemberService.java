package com.kh.even.back.admin.model.service;

import java.util.List;
import com.kh.even.back.admin.model.dto.AdminMemberDTO;
import com.kh.even.back.admin.model.dto.AdminMemberListResponse;
import com.kh.even.back.util.PageInfo;

public interface AdminMemberService {

    /**
     * 회원 목록 조회 (페이징 + 검색) - 개선된 버전
     */
    AdminMemberListResponse getMemberListWithPaging(int currentPage, String keyword);

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