package com.kh.even.back.token.model.vo;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class RefreshToken {
	
	private String token;
	private Long userNo;
	private Long expiration;

}
