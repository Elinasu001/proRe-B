package com.kh.even.back.member.model.vo;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WithdrawMemberVO {
	
	private Long userNo;
	private Long reasonNo;
	private String reasonDetail;
	
}
