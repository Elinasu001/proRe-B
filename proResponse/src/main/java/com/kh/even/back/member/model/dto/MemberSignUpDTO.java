package com.kh.even.back.member.model.dto;

import java.time.LocalDate;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
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
public class MemberSignUpDTO {
	
	@NotBlank(message = "이메일은 필수입니다.")
	@Email(message = "이메일 형식이 올바르지 않습니다.")
	private String email;
	
	@NotBlank(message = "비밀번호는 필수입니다.")
	@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[^A-Za-z0-9\\s])\\S+$", message = "비밀번호는 영문/숫자/특수문자를 모두 사용해야 하고, 공백이 없어야 합니다.")
	@Size(min = 8, max =20, message = "비밀번호는 8~20자 이내여야 합니다.")
	private String userPwd;
	
	@NotBlank(message = "이름은 필수입니다.")
	@Pattern(regexp = "^(?:[가-힣]+|[A-Za-z]+(?:\\s[A-Za-z]+)*)$", message = "이름을 확인해주세요.")
	@Size(min = 2, max = 40, message = "이름은 2~40자 이내여야 합니다.")
	private String userName;
	
	@NotBlank(message = "닉네임은 필수입니다.")
	@Pattern(regexp = "^[가-힣A-Za-z0-9]+$", message = "닉네임은 한글, 영문, 숫자만 입력 가능합니다.")
	@Size(min = 2, max = 20, message = "닉네임은 2~20자 이내여야 합니다.")
	private String nickname;
	
	@NotBlank(message = "휴대폰 번호는 필수입니다.")
	@Pattern(regexp = "^010\\d{8}$", message = "휴대폰 번호는 010으로 시작하는 11자리 숫자만 가능합니다.")
	private String phone;
	
	@NotNull(message = "생년월일은 필수입니다.")
	@Past(message = "생년월일은 과거 날짜여야 합니다.")
	@JsonFormat(pattern = "yyyyMMdd")
	private LocalDate birthday;
	
	@NotBlank(message = "성별은 필수입니다.")
	@Pattern(regexp = "^[MF]$", message = "성별은 M 또는 F만 가능합니다.")
	private String gender;
	
	@NotBlank(message = "우편번호는 필수입니다.")
	@Pattern(regexp = "^[0-9]{5}$", message = "우편번호는 숫자 5자리여야 합니다.")
	private String postcode;
	
	@NotBlank(message = "주소는 필수입니다.")
	@Size(max = 100, message = "주소는 100자 이내로 입력해주세요.")
	private String address;
	
	@NotBlank(message = "상세주소는 필수입니다.")
	@Size(max = 20, message = "상세주소는 20자 이내로 입력해주세요.")
	private String addressDetail;
	
	@NotNull(message = "위도는 필수입니다.")
	@DecimalMin(value = "-90.0", inclusive = true, message = "위도는 -90 이상이어야 합니다.")
	@DecimalMax(value = "90.0", inclusive = true, message = "위도는 90 이하여야 합니다.")
	private Double latitude;
	
	@NotNull(message = "경도는 필수입니다.")
	@DecimalMin(value = "-180.0", inclusive = true, message = "경도는 -180 이상이어야 합니다.")
	@DecimalMax(value = "180.0", inclusive = true, message = "경도는 180 이하여야 합니다.")
	private Double longitude;

}
