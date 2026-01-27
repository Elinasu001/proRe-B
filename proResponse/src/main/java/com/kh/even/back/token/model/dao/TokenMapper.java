package com.kh.even.back.token.model.dao;

import org.apache.ibatis.annotations.Mapper;

import com.kh.even.back.member.model.dto.MemberLogoutDTO;
import com.kh.even.back.token.model.vo.RefreshToken;

@Mapper
public interface TokenMapper {
	
	/**
	 * 로그인 관련 리프레쉬 토큰 삭제
	 * @param userNo
	 * @return 삭제된 행의 개수
	 */
	int deleteTokenByUserNo(Long userNo);
	
	/**
	 * 로그인 성공 시 리프레쉬 토큰 생성
	 * @param refreshToken
	 */
	void saveToken(RefreshToken refreshToken);

	/**
	 * 리프레쉬 토큰 조회
	 * @param token
	 * @return 리프레쉬토큰 객체 반환
	 */
	RefreshToken findByToken(String token);
	
	/**
	 * 로그아웃 관련 리프레쉬 토큰 삭제
	 * @param logoutDTO
	 * @return 삭제된 행의 개수
	 */
	int deleteToken(MemberLogoutDTO logoutDTO);
}
