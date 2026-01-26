package com.kh.even.back.expert.model.vo;

import lombok.Getter;
import lombok.Value;

@Value
@Getter
public class SmallCategoryVO {

	private Long categoryDetailNo;
	private String categoryDetailName;
	private Long categoryNo; // FK 중분류
}
