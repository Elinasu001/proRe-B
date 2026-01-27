package com.kh.even.back.expert.model.dto;

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
public class ExpertSearchDTO {

	private Long expertNo;
	private String nickname;
	private double starScore;
	private int reviewCount;
	private int career;
	private String content;
	private String profileImg;
	private int completedJobs;
	private int totalLikeCount;
	
}
