package com.kh.even.back.estimate.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class EstimateRequestUpdateDTO {

	@NotBlank(message = "설명란은 비어있을 수 없습니다.")
	@Size(max = 2000, message = "설명은 최대 2000자 까지만 허용합니다")
	private String content;

}
