package com.kh.even.back.member.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class WithdrawMemberDTO {
	
	@NotBlank(message = "비밀번호를 입력해주세요.")
	private String password;
	
	@NotNull(message = "탈퇴 사유를 선택해주세요.")
	private Long reasonNo;
	
	@Size(max = 100, message = "탈퇴 사유는 최대 100자까지 입력 가능합니다.")
	private String reasonDetail;
}
