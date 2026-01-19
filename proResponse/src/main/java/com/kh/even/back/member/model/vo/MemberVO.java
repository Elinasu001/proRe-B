package com.kh.even.back.member.model.vo;

import java.sql.Date;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;

@Getter
@AllArgsConstructor
@Value
@Builder
public class MemberVO {
	
	private Long userNo;
	private String email;
	private String userPwd;
	private String userName;
	private String nickname;
	private String profileImgPath;
	private String phone;
	private String birthday;
	private char gender;
	private String postcode;
	private String address;
	private String addressDetail;
	private Double latitude;
	private Double longitude;
	private Date createDate;
	private Date updateDate;
	private Date updatePwdDate;
	private Date deleteDate;
	private String userRole;
	private String penaltyStatus;
	private char status;
}
