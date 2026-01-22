package com.kh.even.back.review.model.vo;

import java.util.List;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ExpertReviewVO {

	
	private String nickname;
	private String[] tagNames;
	private String categoryName;
	private double starScore;
	private String content;
	private String createDate;
	private List<String> filePaths;
}
