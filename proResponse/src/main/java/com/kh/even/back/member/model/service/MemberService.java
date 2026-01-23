package com.kh.even.back.member.model.service;

import org.springframework.web.multipart.MultipartFile;

import com.kh.even.back.auth.model.vo.CustomUserDetails;
import com.kh.even.back.member.model.dto.ChangeEmailDTO;
import com.kh.even.back.member.model.dto.ChangePasswordDTO;
import com.kh.even.back.member.model.dto.MemberSignUpDTO;
import com.kh.even.back.member.model.dto.WithdrawMemberDTO;

public interface MemberService {
	
	void signUp(MemberSignUpDTO member, MultipartFile file);
	
	void changePassword(ChangePasswordDTO password, CustomUserDetails user);
	
	void withdrawMember(WithdrawMemberDTO request, CustomUserDetails user);
	
	void changeEmail(String newEmail, CustomUserDetails user);
	
}
