package com.kh.even.back.member.model.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.kh.even.back.auth.model.vo.CustomUserDetails;
import com.kh.even.back.exception.CustomAuthenticationException;
import com.kh.even.back.exception.CustomServerException;
import com.kh.even.back.exception.EmailAuthFailException;
import com.kh.even.back.exception.EmailDuplicateException;
import com.kh.even.back.exception.NotFoundException;
import com.kh.even.back.exception.PhoneDuplicateException;
import com.kh.even.back.exception.UpdateMemberException;
import com.kh.even.back.file.service.S3Service;
import com.kh.even.back.member.model.dto.ChangePasswordDTO;
import com.kh.even.back.member.model.dto.MemberSignUpDTO;
import com.kh.even.back.member.model.dto.MyProfileDTO;
import com.kh.even.back.member.model.dto.UpdateMeDTO;
import com.kh.even.back.member.model.dto.WithdrawMemberDTO;
import com.kh.even.back.member.model.mapper.MemberMapper;
import com.kh.even.back.member.model.vo.ChangeEmailVO;
import com.kh.even.back.member.model.vo.ChangePasswordVO;
import com.kh.even.back.member.model.vo.MemberVO;
import com.kh.even.back.member.model.vo.MyProfileVO;
import com.kh.even.back.member.model.vo.UpdateMeVO;
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
	
	/**
	 * 이메일 변경
	 */
	public void changeEmail(String newEmail, CustomUserDetails user) {
		// 새 이메일과 기존 이메일 일치여부
		if(newEmail.equals(user.getUsername())) {
			throw new EmailDuplicateException("새 이메일은 기존 이메일과 달라야 합니다.");
		}
		
		// 이메일 중복여부
		checkDuplicatedEmail(newEmail);
		
		// 이메일 인증여부
		String verifiedKey = "email:verified:" + newEmail;
	    if(!redisService.hasKey(verifiedKey)) {
	        throw new CustomAuthenticationException("이메일 인증이 필요합니다.");
	    }
		
	    // DB에 보낼 데이터 가공
	    ChangeEmailVO changeEmail = ChangeEmailVO.builder().email(newEmail)
	    		                                           .userNo(user.getUserNo())
	    		                                           .build();
		// 이메일 변경요청
	    int result = memberMapper.changeEmail(changeEmail);
	    if(result == 0) {
	    	throw new EmailAuthFailException("이메일 변경에 실패했습니다.");
	    }
		
		// 이메일 인증상태 삭제 (1회 인증으로 중복변경 방지)
	 	redisService.deleteValues("email:verified:" + newEmail);
	}
	
	/**
	 * 내정보 수정
	 */
	@Transactional
	public void updateMe(UpdateMeDTO request, MultipartFile file, CustomUserDetails user) {
		String fileUrl = null;
		
		// 연락처가 있을 경우 중복 검사를 한다.
		if(request.getPhone() != null) {
			checkDuplicatedPhone(request.getPhone(), user.getUserNo());
		}
		
		// 프로필 이미지가 존재하면 S3Service.store를 호출한다.
		if(file != null && !file.isEmpty()) {
		    fileUrl = s3Service.store(file, "member");
		}
		
		// 주소 변경을 요청했는가 ? -> 주소 5종 세트 값 전체가 유효한가 ?
		boolean changeAddress = isAddressAttempt(request);
		if(changeAddress) {
			validateAddress(request);
		}
		
		// TB_MEMBER에 전달할 VO
		UpdateMeVO updateVO = UpdateMeVO.builder().userNo(user.getUserNo())
												  .nickname(request.getNickname())
												  .profileImgPath(fileUrl)
												  .phone(request.getPhone())
												  .postcode(request.getPostcode())
												  .address(request.getAddress())
												  .addressDetail(request.getAddressDetail())
												  .latitude(request.getLatitude())
												  .longitude(request.getLongitude())
												  .build();
		
		int result = memberMapper.updateMe(updateVO);
		if(result == 0) {
			throw new UpdateMemberException("내정보 수정에 실패했습니다.");
		}
		
		if(changeAddress) {
			int update = memberMapper.updateLocation(updateVO);
			if(update == 0) {
				throw new UpdateMemberException("회원의 위치정보가 존재하지 않습니다.");
			}
		}
		
	}
	
	/**
	 * 연락처 중복 검사
	 * @param phone
	 */
	private void checkDuplicatedPhone(String phone, Long userNo) {
		int result = memberMapper.countByPhone(phone, userNo);
		if(result > 0) {
			throw new PhoneDuplicateException("이미 사용 중인 연락처입니다.");
		}
	}
	
	/**
	 * 주소변경요청 여부 (하나라도 유효한 값이 있으면 주소변경요청으로 간주)
	 * @param updateDTO
	 * @return true / false
	 */
	private boolean isAddressAttempt(UpdateMeDTO updateDTO) {
		return updateDTO.getPostcode() != null
		    || updateDTO.getAddress() != null
		    || updateDTO.getAddressDetail() != null
		    || updateDTO.getLatitude() != null
		    || updateDTO.getLongitude() != null;
	}
	
	/**
	 * 주소 5종 전체 유효성 검사 (주소변경요청 시 모든 값이 유효한지)
	 * @param updateDTO
	 */
	private void validateAddress(UpdateMeDTO updateDTO) {
	    if (updateDTO.getPostcode() == null
	     || updateDTO.getAddress() == null
	     || updateDTO.getAddressDetail() == null
	     || updateDTO.getLatitude() == null
	     || updateDTO.getLongitude() == null) {
	        throw new IllegalArgumentException("주소 변경 시 우편번호/주소/상세주소/위도/경도는 모두 필수입니다.");
	    }
	}
	
	/**
	 * 내정보 조회
	 */
	public MyProfileDTO getMyProfile(CustomUserDetails user) {
		
		MyProfileVO profileVO = memberMapper.getMyProfile(user.getUserNo());
		if(profileVO == null) {
			throw new NotFoundException("회원 조회에 실패했습니다.");
		}
		
		MyProfileDTO profileDTO = new MyProfileDTO();
		profileDTO.setEmail(profileVO.getEmail());
		profileDTO.setUserName(profileVO.getUserName());
		profileDTO.setNickname(profileVO.getNickname());
		profileDTO.setProfileImage(profileVO.getProfileImage());
		profileDTO.setPostcode(profileVO.getPostcode());
		profileDTO.setAddress(profileVO.getAddress());
		profileDTO.setAddressDetail(profileVO.getAddressDetail());
		profileDTO.setLatitude(profileVO.getLatitude());
		profileDTO.setLongitude(profileVO.getLongitude());
		profileDTO.setPhone(profileVO.getPhone());
		profileDTO.setUserRole(profileVO.getRole());
		
		return profileDTO;
	}
}
