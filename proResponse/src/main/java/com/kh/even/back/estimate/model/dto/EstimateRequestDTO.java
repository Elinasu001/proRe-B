package com.kh.even.back.estimate.model.dto;

import java.sql.Date;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EstimateRequestDTO {

	
	private Date requestDate;

	@NotNull(message="유형은 필수입니다.")
	private String requestType;

	@NotNull(message="요청 서비스는 필수입니다.")
	private String requestService;

	@NotNull(message="요청 상세는 한줄이라도 적어주세요.")
	private String content;

	@NotNull(message="전문가 번호는 필수입니다.")
	private Long expertNo;

	@NotNull(message="카테고리 번호는 필수입니다.")
	private Long categoryDetailNo;

}
