package com.kh.even.back.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.even.back.admin.model.dto.AdminMemberDTO;
import com.kh.even.back.admin.model.dto.AdminMemberListResponse;
import com.kh.even.back.admin.model.dto.AdminPenaltyUpdateRequest;
import com.kh.even.back.admin.model.dto.AdminRoleUpdateRequest;
import com.kh.even.back.admin.model.dto.AdminStatusUpdateRequest;
import com.kh.even.back.admin.model.dto.AdminMemberSearchRequest;
import com.kh.even.back.admin.model.service.AdminMemberService;
import com.kh.even.back.common.ResponseData;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 관리자 회원 관리 컨트롤러
 */
@Slf4j
@RestController
@RequestMapping("/api/admin/members")
@RequiredArgsConstructor
@Validated
public class AdminMemberController {

    private final AdminMemberService adminMemberService;

    /**
     * 회원 목록 조회 (페이징 + 검색)
     * GET /api/admin/members?currentPage=1&keyword=검색어
     */
    @GetMapping
    public ResponseEntity<ResponseData<AdminMemberListResponse>> getMemberList(
        @Valid @ModelAttribute AdminMemberSearchRequest request
    ) {
        log.info("회원 목록 조회 - {}", request);

        AdminMemberListResponse response = adminMemberService.getMemberListWithPaging(
            request.getCurrentPage(),
            request.getKeyword()
        );

        return ResponseData.ok(response);
    }

    /**
     * 회원 상세 조회
     * GET /api/admin/members/{userNo}
     */
    @GetMapping("/{userNo}")
    public ResponseEntity<ResponseData<AdminMemberDTO>> getMemberDetail(
        @PathVariable Long userNo
    ) {
        log.info("회원 상세 조회 - userNo: {}", userNo);

        AdminMemberDTO member = adminMemberService.getMemberDetail(userNo);
        return ResponseData.ok(member);
    }

    /**
     * 회원 상태 변경 (활성/탈퇴)
     * PUT /api/admin/members/{userNo}/status
     */
    @PutMapping("/{userNo}/status")
    public ResponseEntity<ResponseData<String>> updateMemberStatus(
        @PathVariable Long userNo,
        @Valid @RequestBody AdminStatusUpdateRequest request
    ) {
        log.info("회원 상태 변경 - userNo: {}, status: {}", userNo, request.getStatus());

        adminMemberService.updateMemberStatus(userNo, request.getStatus());
        return ResponseData.ok("회원 상태가 변경되었습니다.");
    }

    /**
     * 회원 징계 상태 변경
     * PUT /api/admin/members/{userNo}/penalty
     */
    @PutMapping("/{userNo}/penalty")
    public ResponseEntity<ResponseData<String>> updatePenaltyStatus(
        @PathVariable Long userNo,
        @Valid @RequestBody AdminPenaltyUpdateRequest request
    ) {
        log.info("징계 상태 변경 - userNo: {}, penaltyStatus: {}", userNo, request.getPenaltyStatus());

        adminMemberService.updatePenaltyStatus(userNo, request.getPenaltyStatus());
        return ResponseData.ok("징계 상태가 변경되었습니다.");
    }

    /**
     * 회원 권한 변경
     * PUT /api/admin/members/{userNo}/role
     */
    @PutMapping("/{userNo}/role")
    public ResponseEntity<ResponseData<String>> updateUserRole(
        @PathVariable Long userNo,
        @Valid @RequestBody AdminRoleUpdateRequest request
    ) {
        log.info("권한 변경 - userNo: {}, userRole: {}", userNo, request.getUserRole());

        adminMemberService.updateUserRole(userNo, request.getUserRole());
        return ResponseData.ok("회원 권한이 변경되었습니다.");
    }
}