package com.kh.even.back.admin.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kh.even.back.admin.model.dto.MemberDTO;
import com.kh.even.back.admin.model.service.MemberService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/admin/member")
@RequiredArgsConstructor
public class MemberController {
    
    private final MemberService memberService;
    
    /**
     * 회원 목록 조회 (페이징 + 검색)
     * GET /admin/member/list?currentPage=1&keyword=검색어
     */
    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> getMemberList(
            @RequestParam(defaultValue = "1") int currentPage,
            @RequestParam(required = false) String keyword) {
        
        log.info("회원 목록 조회 요청 - currentPage: {}, keyword: {}", currentPage, keyword);
        
        List<MemberDTO> memberList = memberService.getMemberList(currentPage, keyword);
        int totalCount = memberService.getMemberCount(keyword);
        
        Map<String, Object> response = new HashMap<>();
        response.put("memberList", memberList);
        response.put("totalCount", totalCount);
        response.put("currentPage", currentPage);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * 회원 상세 조회
     * GET /admin/member/{userNo}
     */
    @GetMapping("/{userNo}")
    public ResponseEntity<MemberDTO> getMemberDetail(@PathVariable Long userNo) {
        log.info("회원 상세 조회 요청 - userNo: {}", userNo);
        
        MemberDTO member = memberService.getMemberDetail(userNo);
        
        if (member == null) {
            log.warn("회원을 찾을 수 없음 - userNo: {}", userNo);
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(member);
    }
    
    /**
     * 회원 활성화
     * PATCH /admin/member/{userNo}/activate
     */
    @PatchMapping("/{userNo}/activate")
    public ResponseEntity<Map<String, Object>> activateMember(@PathVariable Long userNo) {
        log.info("회원 활성화 요청 - userNo: {}", userNo);
        
        boolean result = memberService.activateMember(userNo);
        
        return buildResponse(result, "회원이 활성화되었습니다.", "회원 활성화에 실패했습니다.");
    }
    
    /**
     * 회원 비활성화
     * PATCH /admin/member/{userNo}/deactivate
     */
    @PatchMapping("/{userNo}/deactivate")
    public ResponseEntity<Map<String, Object>> deactivateMember(@PathVariable Long userNo) {
        log.info("회원 비활성화 요청 - userNo: {}", userNo);
        
        boolean result = memberService.deactivateMember(userNo);
        
        return buildResponse(result, "회원이 비활성화되었습니다.", "회원 비활성화에 실패했습니다.");
    }
    
    /**
     * 권한 변경
     * PATCH /admin/member/{userNo}/role
     * Request Body: { "userRole": "ROLE_EXPERT" }
     */
    @PatchMapping("/{userNo}/role")
    public ResponseEntity<Map<String, Object>> changeMemberRole(
            @PathVariable Long userNo,
            @RequestBody Map<String, String> request) {
        
        String newRole = request.get("userRole");
        log.info("회원 권한 변경 요청 - userNo: {}, newRole: {}", userNo, newRole);
        
        try {
            boolean result = memberService.changeMemberRole(userNo, newRole);
            return buildResponse(result, "회원 권한이 변경되었습니다.", "회원 권한 변경에 실패했습니다.");
        } catch (IllegalArgumentException e) {
            log.warn("권한 변경 검증 실패 - userNo: {}, error: {}", userNo, e.getMessage());
            return buildErrorResponse(e.getMessage());
        }
    }
    
    /**
     * 징계 적용
     * PATCH /admin/member/{userNo}/penalty/apply
     */
    @PatchMapping("/{userNo}/penalty/apply")
    public ResponseEntity<Map<String, Object>> applyPenalty(@PathVariable Long userNo) {
        log.info("징계 적용 요청 - userNo: {}", userNo);
        
        try {
            boolean result = memberService.applyPenaltyToMember(userNo);
            return buildResponse(result, "징계가 적용되었습니다.", "징계 적용에 실패했습니다.");
        } catch (IllegalArgumentException e) {
            log.warn("징계 적용 검증 실패 - userNo: {}, error: {}", userNo, e.getMessage());
            return buildErrorResponse(e.getMessage());
        }
    }
    
    /**
     * 징계 해제
     * PATCH /admin/member/{userNo}/penalty/remove
     */
    @PatchMapping("/{userNo}/penalty/remove")
    public ResponseEntity<Map<String, Object>> removePenalty(@PathVariable Long userNo) {
        log.info("징계 해제 요청 - userNo: {}", userNo);
        
        try {
            boolean result = memberService.removePenaltyFromMember(userNo);
            return buildResponse(result, "징계가 해제되었습니다.", "징계 해제에 실패했습니다.");
        } catch (IllegalArgumentException e) {
            log.warn("징계 해제 검증 실패 - userNo: {}, error: {}", userNo, e.getMessage());
            return buildErrorResponse(e.getMessage());
        }
    }
    
    /**
     * 성공/실패 응답 생성 (중복 제거)
     */
    private ResponseEntity<Map<String, Object>> buildResponse(
            boolean success, String successMessage, String failMessage) {
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", success);
        response.put("message", success ? successMessage : failMessage);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * 에러 응답 생성
     */
    private ResponseEntity<Map<String, Object>> buildErrorResponse(String errorMessage) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", errorMessage);
        
        return ResponseEntity.badRequest().body(response);
    }
}