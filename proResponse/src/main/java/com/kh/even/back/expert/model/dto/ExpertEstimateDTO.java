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

	@NotNull
	private int price;
	@NotNull
	private String content;
	@NotNull
	private Long requestNo;
	
	
}
