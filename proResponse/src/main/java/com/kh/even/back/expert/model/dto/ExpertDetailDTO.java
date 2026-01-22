package com.kh.even.back.expert.model.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ExpertDetailDTO {

	private Long expertNo;
	private String nickname;
	private String profileImg;
	private int career;
	private String startTime;
	private String endTime;
	private double starScore;
	private String content;
	private int reviewCount;
	private String address;
	private int userLiked;
	private List<String> images;

}
