package com.kh.even.back.expert.model.vo;

import lombok.Getter;
import lombok.Value;

@Value
@Getter
public class MiddleCategoryVO {
	
	private Long categoryNo;
	private String categoryName;
	private Long expertTypeNo; // FK 대분류

}
