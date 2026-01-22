package com.kh.even.back.expert.model.dto;

import jakarta.validation.constraints.NotNull;
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
public class ExpertEstimateDTO {

	@NotNull(message="견적 금액은 필수입니다.")
	private int price;
	@NotNull(message="견적 상세내용은 한줄이라도 적어주세요.")
	private String content;
	@NotNull(message="견적 번호는 필수입니다.")
	private Long requestNo;
	
	
}
