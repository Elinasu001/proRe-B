package com.kh.even.back.member.model.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UpdateMeDTO {
	
	@Pattern(regexp = "^[가-힣A-Za-z0-9]+$", message = "닉네임은 한글, 영문, 숫자만 입력 가능합니다.")
	@Size(min = 2, max = 20, message = "닉네임은 2~20자 이내여야 합니다.")
	private String nickname;
	
	@Pattern(regexp = "^010\\d{8}$", message = "휴대폰 번호는 010으로 시작하는 11자리 숫자만 가능합니다.")
	private String phone;
	
	@Pattern(regexp = "^[0-9]{5}$", message = "우편번호는 숫자 5자리여야 합니다.")
	private String postcode;
	
	@Size(min = 1, message = "공백은 입력할 수 없습니다.")
	@Size(max = 100, message = "주소는 100자 이내로 입력해주세요.")
	private String address;
	
	@Size(min = 1, message = "공백은 입력할 수 없습니다.")
	@Size(max = 20, message = "상세주소는 20자 이내로 입력해주세요.")
	private String addressDetail;
	
	@DecimalMin(value = "-90.0", inclusive = true, message = "위도는 -90 이상이어야 합니다.")
	@DecimalMax(value = "90.0", inclusive = true, message = "위도는 90 이하여야 합니다.")
	private Double latitude;
	
	@DecimalMin(value = "-180.0", inclusive = true, message = "경도는 -180 이상이어야 합니다.")
	@DecimalMax(value = "180.0", inclusive = true, message = "경도는 180 이하여야 합니다.")
	private Double longitude;


}
