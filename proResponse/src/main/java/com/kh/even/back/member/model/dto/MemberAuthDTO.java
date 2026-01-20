package com.kh.even.back.member.model.dto;

import com.kh.even.back.token.dto.TokensDTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 로그인 요청 시에 DB에 존재하는 회원인지 조회 후 결과를 담아줄 용도입니다.
 */
@Getter
@Setter
@Builder
public class MemberAuthDTO {
	
	private Long userNo;
	private String email;
	private String userPwd;
	private String userName;
	private String nickname;
	private String profileImgPath;
	private String userRole;
	private char status;
	
}
