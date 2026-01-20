package com.kh.even.back.token.model.service;

import com.kh.even.back.token.dto.TokensDTO;

public interface TokenService {
	
	TokensDTO generateToken(String username, Long userNo, String role);
	
	
	TokensDTO createTokens(String username, String role);
	
	
	void saveToken(String refreshToken, Long userNo);
	

	TokensDTO validateToken(String refreshToken);

}
