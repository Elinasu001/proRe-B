package com.kh.even.back.expert.model.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LargeCategoryDTO {
	
	private Long expertTypeNo; // 대분류PK
	private String expertName; // ex) 개발
	private List<MiddleCategoryDTO> categories;
}
