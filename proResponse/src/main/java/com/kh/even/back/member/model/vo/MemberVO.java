package com.kh.even.back.member.model.vo;

import java.sql.Date;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberVO {
	
	private Long userNo;
	private String email;
	private String userPwd;
	private String userName;
	private String nickname;
	private String profileImgPath;
	private String phone;
	private Date birthday;
	private char gender;
	private String postcode;
	private String address;
	private String addressDetail;
	private double latitude;
	private double longitude;
	private char status;
	private Date createDate;
	private String role;
}
