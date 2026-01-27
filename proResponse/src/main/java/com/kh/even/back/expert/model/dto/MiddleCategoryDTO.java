package com.kh.even.back.expert.model.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MiddleCategoryDTO {

	private Long categoryNo; // 중분류PK
	private String categoryName; // ex) 백엔드 개발, 프론트엔드 개발 등
	private List<SmallCategoryDTO> categories;
	
}
