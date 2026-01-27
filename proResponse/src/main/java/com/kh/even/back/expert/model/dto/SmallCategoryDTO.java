package com.kh.even.back.expert.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SmallCategoryDTO {
	
	private Long categoryDetailNo; // 소분류 PK
	private String categoryDetailName; // ex) Java, JavaScript 등
	
}
