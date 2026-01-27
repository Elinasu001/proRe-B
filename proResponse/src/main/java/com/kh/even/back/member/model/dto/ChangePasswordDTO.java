package com.kh.even.back.member.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ChangePasswordDTO {
	
	@NotBlank(message = "비밀번호는 필수입니다.")
	@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[^A-Za-z0-9\\s])\\S+$", message = "비밀번호는 영문/숫자/특수문자를 모두 사용해야 하고, 공백이 없어야 합니다.")
	@Size(min = 8, max =20, message = "비밀번호는 8~20자 이내여야 합니다.")
	private String currentPassword;
	
	@NotBlank(message = "비밀번호는 필수입니다.")
	@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[^A-Za-z0-9\\s])\\S+$", message = "비밀번호는 영문/숫자/특수문자를 모두 사용해야 하고, 공백이 없어야 합니다.")
	@Size(min = 8, max =20, message = "비밀번호는 8~20자 이내여야 합니다.")
	private String newPassword;
}
