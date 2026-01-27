package com.kh.even.back.expert.model.dto;

import org.hibernate.validator.constraints.Range;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class ExpertRegisterDTO {
	
	@NotNull(message = "대분류 카테고리를 선택해주세요.")
	@Range(min = 1, max = 3, message = "대분류 카테고리가 올바르지 않습니다.")
	private Long expertTypeNo;
	
	@NotNull(message = "중분류 카테고리를 선택해주세요.")
	@Range(min = 1, max = 7, message = "중분류 카테고리가 올바르지 않습니다.")
	private Long categoryNo;
	
	@NotNull(message = "소분류 카테고리를 선택해주세요.")
	@Range(min = 1, max = 39, message = "소분류 카테고리가 올바르지 않습니다.")
	private Long categoryDetailNo;
	
	@NotNull(message = "경력을 입력해주세요.")
	@Range(min = 1, max = 50, message = "경력은 1~50년 사이여야 합니다.")
	private Integer career;
	
	@NotBlank(message = "연락 시작 시간을 입력해주세요.")
	@Pattern(regexp = "^([01]\\d|2[0-3]):[0-5]\\d$", message = "시작 시간은 00:00 형식이어야 합니다.")
	private String startTime;
	
	@NotBlank(message = "연락 종료 시간을 입력해주세요.")
	@Pattern(regexp = "^([01]\\d|2[0-3]):[0-5]\\d$", message = "종료 시간은 00:00 형식이어야 합니다.")
	private String endTime;
	
	@NotBlank(message = "상세 내용을 입력해주세요.")
	@Size(min = 1, max = 2000, message = "상세 내용은 1~2000자 이내로 입력해주세요.")
	private String content;
	
	@Pattern(regexp = "^\\d{10}$", message = "사업자등록번호는 10자리 숫자입니다.")
	private String businessRegNo;
	
}
