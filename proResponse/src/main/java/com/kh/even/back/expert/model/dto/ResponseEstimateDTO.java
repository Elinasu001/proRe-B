package com.kh.even.back.expert.model.dto;

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
public class ResponseEstimateDTO {

	private Long requestNo;
	private Long expertNo;
	private Long estimateNo;
	private String nickName;
	private double starScore;
	private String profileImg;
	private int reviewCount;
	private int price;
	private String status;
	
}
