package com.kh.even.back.member.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class LogoutRequestDTO {
	
	@NotBlank(message = "refreshToken은 필수입니다.")
	private String refreshToken;
	
}
