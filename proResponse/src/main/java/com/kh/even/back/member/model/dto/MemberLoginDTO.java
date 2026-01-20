package com.kh.even.back.member.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MemberLoginDTO {
	
	@NotBlank(message = "이메일은 필수입니다.")
	@Email(message = "이메일 형식이 올바르지 않습니다.")
	private String email;
	
	@NotBlank(message = "비밀번호는 필수입니다.")
	@Size(min = 8, max =20, message = "비밀번호는 8~20자 이내여야 합니다.")
	private String userPwd;
	
}
