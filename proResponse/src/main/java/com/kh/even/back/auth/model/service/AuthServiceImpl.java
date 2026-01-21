package com.kh.even.back.auth.model.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import com.kh.even.back.auth.model.vo.CustomUserDetails;
import com.kh.even.back.exception.CustomAuthenticationException;
import com.kh.even.back.member.model.dto.LoginResponseDTO;
import com.kh.even.back.member.model.dto.MemberLoginDTO;
import com.kh.even.back.token.dto.TokensDTO;
import com.kh.even.back.token.model.service.TokenService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
	
	private final AuthenticationManager authenticationManager;
	private final TokenService tokenService;

	@Override
	public LoginResponseDTO login(MemberLoginDTO member) {
		
		try {
			
		Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(member.getEmail(), member.getUserPwd()));
		
		CustomUserDetails user = (CustomUserDetails)auth.getPrincipal();
		if(user.getStatus().equals("N")) {
			throw new CustomAuthenticationException("탈퇴한 계정입니다.");
		}
		
		if(user.getPenaltyStatus().equals("Y")) {
			throw new CustomAuthenticationException("정지된 계정입니다.");
		}

		TokensDTO tokens = tokenService.generateToken(user.getUsername(), user.getUserNo(), user.getAuthorities());
		
		String role = user.getAuthorities()
			              .iterator()
			              .next()
			              .getAuthority();
		
		return LoginResponseDTO.builder().tokens(tokens)
									     .userNo(user.getUserNo())
					                     .email(user.getUsername())
					                     .userName(user.getRealName())
					                     .nickname(user.getNickname())
					                     .profileImgPath(user.getProfileImgPath())
					                     .userRole(role)
					                     .status(user.getStatus())
					                     .penaltyStatus(user.getPenaltyStatus())
					                     .build();
		
		} catch (CustomAuthenticationException e) {
            throw e;
			
		} catch(AuthenticationException e) {
			throw new CustomAuthenticationException("회원정보를 확인해주세요.");
			
		} catch (Exception e) {
			log.error("로그인 처리 중 오류", e);
	        throw new CustomAuthenticationException("로그인 처리 중 오류가 발생했습니다.");
	    }
				                   
	}

}
