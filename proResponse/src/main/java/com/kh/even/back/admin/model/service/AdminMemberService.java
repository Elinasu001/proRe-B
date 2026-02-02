package com.kh.even.back.admin.model.service;

import com.kh.even.back.admin.model.dto.AdminMemberDTO;
import com.kh.even.back.admin.model.dto.AdminMemberListResponse;
import com.kh.even.back.admin.model.dto.AdminMemberSearchRequest;
import com.kh.even.back.exception.ResourceNotFoundException;

/**
 * 관리자 회원 관리 서비스 인터페이스
 */
public interface AdminMemberService {

    /**
     * 회원 목록 조회 (페이징 + 검색)
     * 
     * @param currentPage 현재 페이지 번호
     * @param keyword 검색 키워드 (이메일, 이름, 닉네임)
     * @return 회원 목록 + 페이징 정보
     */
	AdminMemberListResponse getMemberListWithPaging(AdminMemberSearchRequest request);

    /**
     * 회원 상세 조회
     * 
     * @param userNo 회원 번호
     * @return 회원 상세 정보
     */
    AdminMemberDTO getMemberDetail(Long userNo);

    /**
     * 회원 상태 변경 (활성/탈퇴)
     * 
     * @param userNo 회원 번호
     * @param status 변경할 상태 ('Y' 활성, 'N' 탈퇴)
     * @throws ResourceNotFoundException 회원을 찾을 수 없는 경우
     */
    void updateMemberStatus(Long userNo, char status);

    /**
     * 회원 징계 상태 변경
     * 
     * @param userNo 회원 번호
     * @param penaltyStatus 징계 상태 ('Y' 징계, 'N' 정상)
     * @throws ResourceNotFoundException 회원을 찾을 수 없는 경우
     */
    void updatePenaltyStatus(Long userNo, char penaltyStatus);

    /**
     * 회원 권한 변경
     * 
     * @param userNo 회원 번호
     * @param userRole 변경할 권한 (ROLE_USER, ROLE_ADMIN 등)
     * @throws ResourceNotFoundException 회원을 찾을 수 없는 경우
     */
    void updateUserRole(Long userNo, String userRole);
}