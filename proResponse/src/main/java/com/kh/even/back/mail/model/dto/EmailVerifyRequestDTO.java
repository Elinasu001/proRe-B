package com.kh.even.back.mail.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * 이메일 인증번호 "검증 요청" DTO
 * 이메일과 인증번호를 함께 서버에 보낼 때 사용
 */
@Getter
@Setter
public class EmailVerifyRequestDTO {
	
	@NotBlank(message = "이메일은 필수 입력값입니다.")
	@Email(message = "올바른 이메일 형식이 아닙니다.")
	private String email;
	
	@NotBlank(message = "인증번호는 필수 입력값입니다.")
	@Size(min = 6, max = 6)
	private String code;
}
