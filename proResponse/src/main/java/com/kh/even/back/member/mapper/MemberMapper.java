package com.kh.even.back.member.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.kh.even.back.member.model.dto.MemberDTO;

@Mapper
public interface MemberMapper {
	
	void signUp(MemberDTO member);	

}
