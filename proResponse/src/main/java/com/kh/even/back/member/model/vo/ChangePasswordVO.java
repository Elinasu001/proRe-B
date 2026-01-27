package com.kh.even.back.member.model.vo;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChangePasswordVO {
	
	private Long userNo;
	private String newPassword;

}
