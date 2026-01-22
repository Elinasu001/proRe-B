package com.kh.even.back.mail.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * 이메일 인증번호 "발송 요청" DTO
 * 이메일만 서버로 보낼 때 사용
 */
@Getter
@Setter
public class EmailSendRequestDTO {
	
	@NotBlank(message = "이메일은 필수 입력값입니다.")
	@Email(message = "올바른 이메일 형식이 아닙니다.")
	private String email;
	
}
