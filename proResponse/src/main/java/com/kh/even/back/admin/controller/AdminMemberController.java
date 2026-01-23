package com.kh.even.back.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kh.even.back.admin.model.dto.AdminMemberDTO;
import com.kh.even.back.admin.model.dto.AdminMemberListResponse;
import com.kh.even.back.admin.model.dto.AdminMemberSearchRequest;
import com.kh.even.back.admin.model.service.AdminMemberService;
import com.kh.even.back.common.ResponseData;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 관리자 회원 관리 컨트롤러
 */
@Slf4j
@Validated  // 추가
@RestController
@RequestMapping("/api/admin/members")
@RequiredArgsConstructor
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
        @PathVariable("userNo") Long userNo  //  수정: name 명시
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
        @PathVariable("userNo") Long userNo,  // 수정: name 명시
        @RequestParam(name = "status")  /// 수정: @RequestBody → @RequestParam
        @NotNull(message = "상태는 필수입니다.")  // 추가: 검증 애노테이션
        Character status
    ) {
        log.info("회원 상태 변경 - userNo: {}, status: {}", userNo, status);

        adminMemberService.updateMemberStatus(userNo, status);
        return ResponseData.ok("회원 상태가 변경되었습니다.");
    }

    /**
     * 회원 징계 상태 변경
     * PUT /api/admin/members/{userNo}/penalty
     */
    @PutMapping("/{userNo}/penalty")
    public ResponseEntity<ResponseData<String>> updatePenaltyStatus(
        @PathVariable("userNo") Long userNo,  //  수정: name 명시
        @RequestParam(name = "penaltyYn")  //  수정: @RequestBody → @RequestParam
        @NotNull(message = "징계 상태는 필수입니다.")  //  추가: 검증 애노테이션
        Character penaltyYn
    ) {
        log.info("징계 상태 변경 - userNo: {}, penaltyYn: {}", userNo, penaltyYn);

        adminMemberService.updatePenaltyStatus(userNo, penaltyYn);
        return ResponseData.ok("징계 상태가 변경되었습니다.");
    }

    /**
     * 회원 권한 변경
     * PUT /api/admin/members/{userNo}/role
     */
    @PutMapping("/{userNo}/role")
    public ResponseEntity<ResponseData<String>> updateUserRole(
        @PathVariable("userNo") Long userNo,  // 수정: name 명시
        @RequestParam(name = "userRole")  // 수정: @RequestBody → @RequestParam
        @NotBlank(message = "권한은 필수입니다.")  // 추가: 검증 애노테이션
        String userRole
    ) {
        log.info("권한 변경 - userNo: {}, userRole: {}", userNo, userRole);

        adminMemberService.updateUserRole(userNo, userRole);
        return ResponseData.ok("회원 권한이 변경되었습니다.");
    }
}