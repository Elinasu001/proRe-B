package com.kh.even.back.estimate.model.dto;

import java.util.List;

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
public class EstimateResponseDetailDTO {

	private Long requestNo;
	private Long expertNo;
	private String nickname;
	private String profileImg;
	private int price;
	private String startTime;
	private String endTime;
	private int career;
	private String content;
	private List<String> filePaths;
	
}
