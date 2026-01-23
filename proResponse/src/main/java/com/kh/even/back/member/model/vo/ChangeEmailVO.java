package com.kh.even.back.member.model.vo;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChangeEmailVO {
	
	private String email;
	private Long userNo;
}
