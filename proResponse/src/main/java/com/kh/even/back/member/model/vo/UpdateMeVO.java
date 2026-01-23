package com.kh.even.back.member.model.vo;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdateMeVO {

	private Long userNo;
	
	private String nickname;
	private String profileImgPath;
	private String phone;
	
	private String postcode;
	private String address;
	private String addressDetail;
	
	private Double latitude;
	private Double longitude;
	
}
