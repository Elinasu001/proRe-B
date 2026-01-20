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
public class ExpertDTO {

	private Long expertNo;
	private String nickName;
	private double starScore;
	private int reviewCount;
	private String address;
	private String startTime;
	private String endTime;
    private String requestStatus;
	
}
