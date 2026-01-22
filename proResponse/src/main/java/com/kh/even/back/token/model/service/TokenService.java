package com.kh.even.back.token.model.service;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

import com.kh.even.back.member.model.dto.MemberLogoutDTO;
import com.kh.even.back.token.dto.TokensDTO;

public interface TokenService {
	
	TokensDTO generateToken(String username, Long userNo, Collection<? extends GrantedAuthority> role);
	
	TokensDTO createTokens(String username, String role);
	
	void saveToken(String refreshToken, Long userNo);
	
	TokensDTO validateToken(String refreshToken);
	
	void deleteToken(MemberLogoutDTO logoutDTO);
}
