package com.kh.even.back.member.model.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 내정보 조회 + 권한 조회 응답용 DTO
 */
@Getter
@Setter
public class MyProfileDTO {
	
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
	private String userRole;

}
