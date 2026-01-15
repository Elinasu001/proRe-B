package com.kh.even.back.auth.model.service;

import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.kh.even.back.auth.model.vo.CustomUserDetails;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
	// AuthenticationManger가 실질적으로 사용자의 정보를 조회할 때 메소드를 호출하는 클래스

	// 멤버 맵퍼 들어가야함

	@Override
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {

		// log.info("userId : {}" , userId);

		//MemberDTO user = mapper.loadUser(userId);

//		if (user == null) {
//			throw new UsernameNotFoundException("유저 결과가 없습니다.");
//		}
//		return CustomUserDetails.builder().userNo(user.getUserNo()).username(user.getMemberId())
//				.password(user.getMemberPwd()).realName(user.getMemberName()).birthDay(user.getBirthDay())
//				.email(user.getEmail()).phone(user.getPhone()).licenseUrl(user.getLicenseUrl())
//				.authorities(Collections.singletonList(new SimpleGrantedAuthority(user.getRole()))).build();
		return null;

	}

}