package com.kh.even.back.expert.model.vo;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ExpertRegisterVO {
	
	private Long userNo;
	private Integer career;
	private String startTime;
	private String endTime;
	private String content;
	private Long expertTypeNo;
	
}
