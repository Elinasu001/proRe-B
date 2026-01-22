package com.kh.even.back.member.model.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.kh.even.back.auth.model.vo.CustomUserDetails;
import com.kh.even.back.exception.CustomAuthenticationException;
import com.kh.even.back.exception.CustomServerException;
import com.kh.even.back.exception.EmailDuplicateException;
import com.kh.even.back.file.service.S3Service;
import com.kh.even.back.member.model.dto.ChangePasswordDTO;
import com.kh.even.back.member.model.dto.MemberSignUpDTO;
import com.kh.even.back.member.model.dto.WithdrawMemberDTO;
import com.kh.even.back.member.model.mapper.MemberMapper;
import com.kh.even.back.member.model.vo.ChangePasswordVO;
import com.kh.even.back.member.model.vo.MemberVO;
import com.kh.even.back.member.model.vo.WithdrawMemberVO;
import com.kh.even.back.redis.RedisService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

	private final MemberMapper memberMapper;
	private final PasswordEncoder passwordEncoder;
	private final S3Service s3Service;
	private final RedisService redisService;
		
	/**
	 * 회원가입
	 */
	@Override
	@Transactional
	public void signUp(MemberSignUpDTO member, MultipartFile file) {
		String fileUrl = null;
		
		// 이메일 인증 완료 시에만 회원가입이 가능하게 한다.
		String verifiedKey = "email:verified:" + member.getEmail();
	    if(!redisService.hasKey(verifiedKey)) {
	        throw new CustomAuthenticationException("이메일 인증이 필요합니다.");
	    }
	    
		// 이메일 중복 검사 (프론트 + 백엔드)
		checkDuplicatedEmail(member.getEmail());
		
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
			
			// 가입 성공 후 이메일 인증상태 삭제(1회 인증으로 중복가입 방지)
			redisService.deleteValues("email:verified:" + member.getEmail());
	}
	
	/**
	 * 비밀번호 변경
	 */
	@Override
	public void changePassword(ChangePasswordDTO password, CustomUserDetails user) {
	
		validatePassword(password.getCurrentPassword(), user);
		
		if (passwordEncoder.matches(password.getNewPassword(), user.getPassword())) {
	        throw new CustomAuthenticationException("새 비밀번호는 기존 비밀번호와 달라야 합니다.");
	    }
		
		String newPassword = passwordEncoder.encode(password.getNewPassword());
		
		ChangePasswordVO passwordVO = ChangePasswordVO.builder().userNo(user.getUserNo())
							                          .newPassword(newPassword)
							                          .build();
		
		memberMapper.changePassword(passwordVO);
		
	}
	
	/**
	 * 비밀번호 검증
	 */
	private void validatePassword(String password, CustomUserDetails user) {
		
		String encodedPassword = user.getPassword();
		
		if(!passwordEncoder.matches(password, encodedPassword)) {
			throw new CustomAuthenticationException("일치하지 않는 비밀번호");
		}
		
	}
	
	/**
	 * 회원탈퇴
	 */
	@Transactional
	public void withdrawMember(WithdrawMemberDTO request, CustomUserDetails user) {
		WithdrawMemberVO withdrawMember = saveWithdrawRequest(request, user);
		updateMemberStatus(withdrawMember);
	}
	
	
	/**
	 * 회원탈퇴 요청 저장하기
	 * @param request (PK / 회원탈퇴사유번호 / 상세사유)
	 * @param user (회원정보)
	 * @return 회원탈퇴용 VO 반환
	 */
	private WithdrawMemberVO saveWithdrawRequest(WithdrawMemberDTO request, CustomUserDetails user) {
		validatePassword(request.getPassword(), user);
		
		WithdrawMemberVO withdrawMember = WithdrawMemberVO.builder().userNo(user.getUserNo())
				  										 .reasonNo(request.getReasonNo())
				  										 .reasonDetail(request.getReasonDetail())
				  										 .build();
		if(!"Y".equals(user.getStatus())) {
			throw new CustomAuthenticationException("이미 탈퇴 처리된 회원입니다.");
		}
		if(!"N".equals(user.getPenaltyStatus())) {
			throw new CustomAuthenticationException("비활성화된 계정입니다.");
		}
		
		int insertRows = memberMapper.saveWithdrawRequest(withdrawMember);
		if(insertRows == 0) {
			throw new CustomServerException("회원탈퇴 요청에 실패했습니다.");
		}
		
		return withdrawMember;
	}
	
	/**
	 * 회원 상태 수정(논리 삭제)
	 * @param withdrawMember (회원탈퇴용 VO)
	 */
	private void updateMemberStatus(WithdrawMemberVO withdrawMember) {
		
		int updateRows = memberMapper.updateMemberStatus(withdrawMember);
		if(updateRows == 0) {
			throw new CustomServerException ("회원탈퇴에 실패했습니다.");
		}
		
	}
	
	/**
	 * 이메일 중복 검사
	 * @param email
	 */
	private void checkDuplicatedEmail(String email) {
				
				int count = memberMapper.countByEmail(email);
					
				if(count >= 1) {
					throw new EmailDuplicateException("이미 사용 중인 이메일입니다.");
				}
	}
	
}
