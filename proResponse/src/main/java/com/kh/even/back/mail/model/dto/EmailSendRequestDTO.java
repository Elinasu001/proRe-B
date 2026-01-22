package com.kh.even.back.mail.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailSendRequestDTO {
	
	@NotBlank
	@Email
	private String email;
	
}
