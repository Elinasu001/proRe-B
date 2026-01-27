package com.kh.even.back.category.model.dto;

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
public class ExpertListDTO {

	private Long expertNo;
	private String nickName;
	private String profileImg;
	private int career;
	private String startTime;
	private String endTime;
	private double starScore;
	private int reviewCount;
	private String address;
	private int userLiked;
	private int totalLikes;
	private int completedJobs;
	
}
