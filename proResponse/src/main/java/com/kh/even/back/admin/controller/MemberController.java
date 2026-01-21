package com.kh.even.back.admin.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
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
import com.kh.even.back.common.ResponseData;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController("adminMemberController") // Bean 명칭 중복 피하기 위해 별도 정의
@RequestMapping("/api/admin/members")  // ✅ 복수형
@RequiredArgsConstructor
public class MemberController {
    
    private final MemberService memberService;
    
    /**
     * 회원 목록 조회 (페이징 + 검색)
     * GET /api/admin/members/list?currentPage=1&keyword=검색어
     */
    @GetMapping("/list")
    public ResponseEntity<ResponseData<Map<String, Object>>> getMemberList(
            @RequestParam(name = "currentPage", defaultValue = "1") int currentPage,
            @RequestParam(name = "keyword", required = false) String keyword) {
        
        log.info("회원 목록 조회 요청 - currentPage: {}, keyword: {}", currentPage, keyword);
        
        List<MemberDTO> memberList = memberService.getMemberList(currentPage, keyword);
        int totalCount = memberService.getMemberCount(keyword);
        
        Map<String, Object> data = new HashMap<>();
        data.put("memberList", memberList);
        data.put("totalCount", totalCount);
        data.put("currentPage", currentPage);
        
        return ResponseData.ok(data);
    }
    
    /**
     * 회원 상세 조회
     * GET /api/admin/members/{userNo}
     */
    @GetMapping("/{userNo}")
    public ResponseEntity<ResponseData<MemberDTO>> getMemberDetail(@PathVariable Long userNo) {
        log.info("회원 상세 조회 요청 - userNo: {}", userNo);
        
        MemberDTO member = memberService.getMemberDetail(userNo);
        
        if (member == null) {
            log.warn("회원을 찾을 수 없음 - userNo: {}", userNo);
            return ResponseData.failure("회원을 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
        }
        
        return ResponseData.ok(member);
    }
    
    /**
     * 회원 활성화
     * PATCH /api/admin/members/{userNo}/activate
     */
    @PatchMapping("/{userNo}/activate")
    public ResponseEntity<ResponseData<Void>> activateMember(@PathVariable Long userNo) {
        log.info("회원 활성화 요청 - userNo: {}", userNo);
        
        try {
            boolean result = memberService.activateMember(userNo);
            
            if (result) {
                return ResponseData.ok(null, "회원이 활성화되었습니다.");
            } else {
                return ResponseData.failure("회원 활성화에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (IllegalArgumentException e) {
            log.warn("회원 활성화 검증 실패 - userNo: {}, error: {}", userNo, e.getMessage());
            return ResponseData.failure(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    /**
     * 회원 비활성화
     * PATCH /api/admin/members/{userNo}/deactivate
     */
    @PatchMapping("/{userNo}/deactivate")
    public ResponseEntity<ResponseData<Void>> deactivateMember(@PathVariable Long userNo) {
        log.info("회원 비활성화 요청 - userNo: {}", userNo);
        
        try {
            boolean result = memberService.deactivateMember(userNo);
            
            if (result) {
                return ResponseData.ok(null, "회원이 비활성화되었습니다.");
            } else {
                return ResponseData.failure("회원 비활성화에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (IllegalArgumentException e) {
            log.warn("회원 비활성화 검증 실패 - userNo: {}, error: {}", userNo, e.getMessage());
            return ResponseData.failure(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    /**
     * 권한 변경
     * PATCH /api/admin/members/{userNo}/role
     * Request Body: { "userRole": "ROLE_EXPERT" }
     */
    @PatchMapping("/{userNo}/role")
    public ResponseEntity<ResponseData<Void>> changeMemberRole(
            @PathVariable Long userNo,
            @RequestBody Map<String, String> request) {
        
        String newRole = request.get("userRole");
        log.info("회원 권한 변경 요청 - userNo: {}, newRole: {}", userNo, newRole);
        
        try {
            boolean result = memberService.changeMemberRole(userNo, newRole);
            
            if (result) {
                return ResponseData.ok(null, "회원 권한이 변경되었습니다.");
            } else {
                return ResponseData.failure("회원 권한 변경에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (IllegalArgumentException e) {
            log.warn("권한 변경 검증 실패 - userNo: {}, error: {}", userNo, e.getMessage());
            return ResponseData.failure(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    /**
     * 징계 적용
     * PATCH /api/admin/members/{userNo}/penalty/apply
     */
    @PatchMapping("/{userNo}/penalty/apply")
    public ResponseEntity<ResponseData<Void>> applyPenalty(@PathVariable Long userNo) {
        log.info("징계 적용 요청 - userNo: {}", userNo);
        
        try {
            boolean result = memberService.applyPenaltyToMember(userNo);
            
            if (result) {
                return ResponseData.ok(null, "징계가 적용되었습니다.");
            } else {
                return ResponseData.failure("징계 적용에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (IllegalArgumentException e) {
            log.warn("징계 적용 검증 실패 - userNo: {}, error: {}", userNo, e.getMessage());
            return ResponseData.failure(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    /**
     * 징계 해제
     * PATCH /api/admin/members/{userNo}/penalty/remove
     */
    @PatchMapping("/{userNo}/penalty/remove")
    public ResponseEntity<ResponseData<Void>> removePenalty(@PathVariable Long userNo) {
        log.info("징계 해제 요청 - userNo: {}", userNo);
        
        try {
            boolean result = memberService.removePenaltyFromMember(userNo);
            
            if (result) {
                return ResponseData.ok(null, "징계가 해제되었습니다.");
            } else {
                return ResponseData.failure("징계 해제에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (IllegalArgumentException e) {
            log.warn("징계 해제 검증 실패 - userNo: {}, error: {}", userNo, e.getMessage());
            return ResponseData.failure(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}