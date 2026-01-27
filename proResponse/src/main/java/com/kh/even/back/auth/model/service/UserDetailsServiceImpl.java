package com.kh.even.back.auth.model.service;

import java.util.Collections;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.kh.even.back.auth.model.vo.CustomUserDetails;
import com.kh.even.back.member.model.vo.MemberAuthVO;
import com.kh.even.back.member.model.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
	
	private final MemberMapper memberMapper;  // ✅ 필수!
	
	// AuthenticationManager가 실질적으로 사용자의 정보를 조회할 때 메소드를 호출하는 클래스
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		MemberAuthVO member = memberMapper.loadUser(email);
		// log.info("매퍼 호출 후 결과 : {}", member);
		
		if(member == null) {
			throw new UsernameNotFoundException("회원정보가 존재하지 않습니다.");
		}
		
		return CustomUserDetails.builder()
				.userNo(member.getUserNo())
				.username(member.getEmail())
				.password(member.getUserPwd())
				.realName(member.getUserName())
				.nickname(member.getNickname())
				.profileImgPath(member.getProfileImgPath())
				.status(member.getStatus())
				.penaltyStatus(member.getPenaltyStatus())
				.authorities(Collections.singletonList(new SimpleGrantedAuthority(member.getUserRole())))
				.build();
	}
}