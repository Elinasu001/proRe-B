package com.kh.even.back.member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kh.even.back.auth.model.vo.CustomUserDetails;
import com.kh.even.back.common.ResponseData;
import com.kh.even.back.member.model.dto.ChangePasswordDTO;
import com.kh.even.back.member.model.dto.MemberSignUpDTO;
import com.kh.even.back.member.model.dto.UpdateMeDTO;
import com.kh.even.back.member.model.dto.WithdrawMemberDTO;
import com.kh.even.back.member.model.service.MemberService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {
	
	private final MemberService memberService;
	
	@PostMapping
	public ResponseEntity<ResponseData<Void>> signUp(@Valid @ModelAttribute MemberSignUpDTO member, @RequestParam(name = "profileImg", required = false) MultipartFile file) {
		// log.info("회원가입 요청 진위여부 : {}", member);
		
		memberService.signUp(member, file);
		
		return ResponseData.created(null, "회원가입에 성공했습니다.");
	}
	
	@PutMapping("/me/password")
	public ResponseEntity<ResponseData<Void>> changePassword(@Valid @RequestBody ChangePasswordDTO password, @AuthenticationPrincipal CustomUserDetails user) {
		// log.info("비밀번호 정보 : {}", password);
		
		memberService.changePassword(password, user);
		
		return ResponseData.ok(null, "비밀번호 변경에 성공했습니다.");
	}
	
	@DeleteMapping
	public ResponseEntity<ResponseData<Void>> withdrawMember(@Valid @RequestBody WithdrawMemberDTO request, @AuthenticationPrincipal CustomUserDetails user) {
		// log.info("회원탈퇴 요청 진위여부 : {}", request);
		
		memberService.withdrawMember(request, user);
		
		return ResponseData.ok(null, "회원탈퇴에 성공했습니다.");
	}
	
	@PutMapping("/me/email")
	public ResponseEntity<ResponseData<Void>> changeEmail(@RequestParam(name = "newEmail") @NotBlank(message = "이메일은 필수입니다.") @Email(message = "이메일 형식이 올바르지 않습니다.") 
														  String newEmail, 
													      @AuthenticationPrincipal CustomUserDetails user) {
	
		memberService.changeEmail(newEmail, user);
		
		return ResponseData.ok(null, "이메일 변경에 성공했습니다."); 
	}
	
	@PatchMapping("/me")
	public ResponseEntity<ResponseData<Void>> UpdateMe(@Valid @ModelAttribute UpdateMeDTO request, @RequestParam(name = "profileImg", required = false) MultipartFile file,
			 										   @AuthenticationPrincipal CustomUserDetails user) {
		
		memberService.updateMe(request, file, user);
		
		return ResponseData.ok(null, "정보변경에 성공했습니다.");
	}
}
