package com.kh.even.back.member.model.dto;

import com.kh.even.back.token.dto.TokensDTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LoginResponseDTO {

	private TokensDTO tokens;
	private Long userNo;
	private String email;
	private String userName;
	private String nickname;
	private String profileImgPath;
	private String userRole;
	private String status;
	private String penaltyStatus;
		
	
}
