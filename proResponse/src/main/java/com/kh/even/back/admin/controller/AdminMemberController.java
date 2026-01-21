package com.kh.even.back.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.even.back.admin.model.dto.AdminMemberListResponse;
import com.kh.even.back.admin.model.dto.MemberSearchRequest;
import com.kh.even.back.admin.model.service.AdminMemberService;
import com.kh.even.back.common.ResponseData;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/admin/members")
@RequiredArgsConstructor
@Validated  // ✅ 추가
public class AdminMemberController {

    private final AdminMemberService adminMemberService;

    @GetMapping
    public ResponseEntity<ResponseData<AdminMemberListResponse>> getMemberList(
        @Valid @ModelAttribute MemberSearchRequest request  // ✅ 변경
    ) {
        log.info("회원 목록 조회 - {}", request);

        AdminMemberListResponse response = adminMemberService.getMemberListWithPaging(
            request.getCurrentPage(), 
            request.getKeyword()
        );
        
        return ResponseData.ok(response);
    }
}