package com.kh.even.back.auth.model.vo;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CustomUserDetails implements UserDetails {

	private Long userNo;
	private String username; // ID로 쓰는거 이메일로 바꾸시면됨 
	private String password;
	private String realName; // 실명
	private String birthDay;
	private String email;
	private String phone;
	private String licenseUrl;
	private Collection<? extends GrantedAuthority> authorities;

}
