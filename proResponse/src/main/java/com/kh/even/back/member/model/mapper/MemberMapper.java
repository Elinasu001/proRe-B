package com.kh.even.back.member.model.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import com.kh.even.back.member.model.vo.MemberVO;

@Mapper
public interface MemberMapper {
	
	/**
	 * 이메일 중복확인
	 * @param email
	 * @return
	 */
	int countByEmail(String email);
	
	/**
	 * 회원가입
	 * @param member
	 */
	int  signUp(MemberVO member);
	
	
	@Insert("INSERT INTO TB_MEMBER_LOCATION (USER_NO,LAT,LNG) VALUES(#{userNo},#{latitude},#{longitude})")
	int insertLocation(MemberVO member);

}
