package com.kh.even.back.member.model.service;

import org.springframework.web.multipart.MultipartFile;

import com.kh.even.back.member.model.dto.MemberSignUpDTO;

public interface MemberService {
	
	void signUp(MemberSignUpDTO member, MultipartFile file);

}
