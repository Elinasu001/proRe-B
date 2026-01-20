package com.kh.even.back.token.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class TokensDTO {
	
	private String AccessToken;
	private String RefreshToken;

}
