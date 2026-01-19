package com.kh.even.back.member.model.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import com.kh.even.back.member.model.vo.MemberVO;

@Mapper
public interface MemberMapper {
	
	/**
	 * 이메일 중복확인
	 * @param email
	 * @return DB에 존재하는 동일한 이메일의 개수를 반환해줍니다.
	 */
	int countByEmail(String email);
	
	/**
	 * 회원가입
	 * @param member
	 * @return DB에 새로 추가된 행의 개수를 반환해줍니다.
	 */
	int signUp(MemberVO member);
	
	/**
	 * 회원 위도/경도 추가
	 * @param member
	 * @return DB에 새로 추가된 행의 개수를 반환해줍니다.
	 */
	int saveLocation(MemberVO member);

}
