package com.kh.even.back.estimate.model.dto;

import java.sql.Date;

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
public class EstimateReqeustDTO {

	private Date requestDate;
	private String type;
	private String requestService;
	private String content;
	private Long expertNo;
	private Long categoryDeatilNo;
	
}
