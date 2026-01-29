package com.kh.even.back.review.model.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ExpertReviewDTO {

	private Long reviewNo;
	private String nickname;
	private String tagNames;
	private String categoryName;
	private double starScore;
	private String content;
	private String createDate;
	private List<String> filePaths;	
	private String profileImg;
	
}
