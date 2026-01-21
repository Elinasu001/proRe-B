package com.kh.even.back.member.model.service;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.kh.even.back.auth.model.vo.CustomUserDetails;
import com.kh.even.back.exception.CustomAuthenticationException;
import com.kh.even.back.exception.EmailDuplicateException;
import com.kh.even.back.file.service.S3Service;
import com.kh.even.back.member.model.dto.ChangePasswordDTO;
import com.kh.even.back.member.model.dto.MemberSignUpDTO;
import com.kh.even.back.member.model.mapper.MemberMapper;
import com.kh.even.back.member.model.vo.ChangePasswordVO;
import com.kh.even.back.member.model.vo.MemberVO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
	
	private final MemberMapper memberMapper;
	private final PasswordEncoder passwordEncoder;
	private final S3Service s3Service;
	
	@Override
	@Transactional
	public void signUp(MemberSignUpDTO member, MultipartFile file) {
		String fileUrl = null;
		
		// 유효성 검사 => Validator에게 위임
		
		// 이메일 중복 검사 (프론트 + 백엔드)
		int count = memberMapper.countByEmail(member.getEmail());
		// log.info("이메일 중복여부 : {}", count);
			
		
		if(count >= 1) {
			throw new EmailDuplicateException("이미 사용 중인 이메일입니다.");
		}
		
		
		// 프로필 이미지가 존재하면 S3Service.store를 호출한다.
		if(file != null && !file.isEmpty()) {
			 fileUrl = s3Service.store(file, "member");
			// log.info("파일명 : {}", fileUrl);
		}
			
		
		
		MemberVO memberVO = MemberVO.builder()
				                         .email(member.getEmail())
				                         .userPwd(passwordEncoder.encode(member.getUserPwd())) // 비밀번호 암호화
				                         .userName(member.getUserName())
				                         .nickname(member.getNickname())
				                         .phone(member.getPhone())
				                         .birthday(member.getBirthday())
				                         .gender(member.getGender().charAt(0)) // DTO에서는 String -> VO에는 char
				                         .postcode(member.getPostcode())
				                         .address(member.getAddress())
				                         .addressDetail(member.getAddressDetail())
				                         .latitude(member.getLatitude())
				                         .longitude(member.getLongitude())
				                         .profileImgPath(fileUrl) // 프로필 이미지 URL 또는 KEY
				                         .build();
				                         
		
		// 매퍼 호출
		int memberResult = memberMapper.signUp(memberVO);
		   if (memberResult != 1) {
	            throw new IllegalStateException("회원가입 처리에 실패했습니다.");
	        }
		
		   
		   // log.info("{}" , memberVO);	   
		   
		int locationResult = memberMapper.saveLocation(memberVO);
			if (locationResult != 1) {
				throw new IllegalStateException("위치정보 저장에 실패했습니다.");
			}
	}
	
	@Override
	public void changePassword(ChangePasswordDTO password) {
		// 현재 비밀번호가 맞는지 검증 -> passwordEncoder.matches(평문, 암호문)
		// Authentication에서 현재 인증된 사용자의 정보 뽑아오기
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		CustomUserDetails user = (CustomUserDetails)auth.getPrincipal();
		
		String currentPassword = password.getCurrentPassword();
		String encodedPassword = user.getPassword();
		if(!passwordEncoder.matches(currentPassword, encodedPassword)) {
			throw new CustomAuthenticationException("일치하지 않는 비밀번호");
		}
		// 현재 비밀번호가 맞다면 새 비밀번호를 암호화
		String newPassword = passwordEncoder.encode(password.getNewPassword());
		
		// UPDATE TB_MEMBER MEMBER_PWD = #{newpassword} WHERE USER_NO = #{userNo}
		ChangePasswordVO passwordVO = ChangePasswordVO.builder().userNo(user.getUserNo())
							                          .newPassword(newPassword)
							                          .build();
		
		memberMapper.changePassword(passwordVO);
		
	}
	
	@Override
	public void deleteByPassword(String password) {
		
	}
}
