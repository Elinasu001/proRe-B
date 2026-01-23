package com.kh.even.back.auth.model.service;

import com.kh.even.back.auth.model.vo.CustomUserDetails;
import com.kh.even.back.member.model.dto.LoginResponseDTO;
import com.kh.even.back.member.model.dto.LogoutRequestDTO;
import com.kh.even.back.member.model.dto.MemberLoginDTO;

public interface AuthService {
	
	LoginResponseDTO login(MemberLoginDTO member);
	
	void logout(LogoutRequestDTO request, CustomUserDetails member);

}
