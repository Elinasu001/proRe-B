package com.kh.even.back.member.model.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.kh.even.back.member.model.vo.ChangeEmailVO;
import com.kh.even.back.member.model.vo.ChangePasswordVO;
import com.kh.even.back.member.model.vo.MemberAuthVO;
import com.kh.even.back.member.model.vo.MemberVO;
import com.kh.even.back.member.model.vo.UpdateMeVO;
import com.kh.even.back.member.model.vo.WithdrawMemberVO;

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
	
	/**
	 * 로그인 요청 시 회원정보 조회
	 * @param email
	 * @return PK / 이메일 / 패스워드 / 이름 / 닉네임 / 프로필 이미지 / 권한 / 상태를 DTO 형태로 반환해줍니다.
	 */
	MemberAuthVO loadUser(String email);

	/**
	 * 비밀번호 변경
	 * @param passwordVO
	 */
	void changePassword(ChangePasswordVO passwordVO);
	
	/**
	 * 회원탈퇴 요청
	 * @param wmv
	 * @return TB_MEMBER_WITHDRAW에 새로 추가된 행의 개수를 반환해줍니다.
	 */
	int saveWithdrawRequest(WithdrawMemberVO withdrawMember);
	
	/**
	 * 회원탈퇴(논리 삭제)
	 * @param userNo
	 * @return TB_MEMBER에 업데이트된 행의 개수를 반환해줍니다.
	 */
	int updateMemberStatus(WithdrawMemberVO withdrawMember);
	
	/**
	 * 이메일 변경
	 * @param changeEmail 새로운 이메일과 회원PK
	 * @return TB_MEMBER에 업데이트 된 행의 개수를 반환해줍니다.
	 */
	int changeEmail(ChangeEmailVO changeEmail);
	
	/**
	 * 연락처 중복검사
	 * @param phone
	 * @return DB에 존재하는 동일한 연락처의 개수를 반환해줍니다.
	 */
	int countByPhone(String phone);
	
	/**
	 * 내정보 수정
	 * @param updateVO
	 * @return 업데이트된 행의 개수를 반환해줍니다.
	 */
	int updateMe(UpdateMeVO updateVO);
	
	/**
	 * 회원 위도/경도 수정
	 * @param updateVO
	 * @return 업데이트된 행의 개수를 반환해줍니다.
	 */
	int updateLocation(UpdateMeVO updateVO);
}
