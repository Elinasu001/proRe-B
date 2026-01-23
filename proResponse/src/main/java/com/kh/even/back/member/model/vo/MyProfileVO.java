package com.kh.even.back.member.model.vo;

import lombok.Builder;
import lombok.Getter;

/**
 * 내정보 조회 + 권한 조회를 위해 DB에서 값을 받아오는 용도
 */
@Getter
@Builder
public class MyProfileVO {
	
	private Long userNo;
	private String email;
	private String userName;
	private String nickname;
	private String profileImage;
	private String postcode;
	private String address;
	private String addressDetail;
	private Double latitude;
	private Double longitude;
	private String phone;
	private String role;

}
