package com.kh.even.back.admin.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kh.even.back.admin.model.dto.AdminMemberDTO;
import com.kh.even.back.admin.model.service.AdminMemberService;
import com.kh.even.back.common.ResponseData;  // ✅ 수정

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/admin/members")
@RequiredArgsConstructor
public class AdminMemberController {
    
    private final AdminMemberService adminMemberService;
    
    @GetMapping
    public ResponseEntity<ResponseData<Map<String, Object>>> getMemberList(  // ✅ 반환 타입 수정
        @RequestParam(name = "currentPage", defaultValue = "1") int currentPage,
        @RequestParam(name = "keyword", required = false) String keyword
    ) {
        log.info("회원 목록 조회 - currentPage: {}, keyword: {}", currentPage, keyword);
        
        try {
            List<AdminMemberDTO> memberList = adminMemberService.getMemberList(currentPage, keyword);
            int totalCount = adminMemberService.getMemberCount(keyword);
            
            Map<String, Object> data = new HashMap<>();
            data.put("memberList", memberList);
            data.put("totalCount", totalCount);
            data.put("currentPage", currentPage);
            
            return ResponseData.ok(data);  // ✅ 수정
            
        } catch (Exception e) {
            log.error("회원 목록 조회 실패", e);
            return ResponseData.failure(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);  // ✅ 수정
        }
    }
}