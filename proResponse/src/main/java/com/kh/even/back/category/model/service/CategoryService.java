package com.kh.even.back.category.model.service;

import java.util.List;

import com.kh.even.back.category.model.dto.CategoryDTO;
import com.kh.even.back.category.model.entity.CategoryEntity;

public interface CategoryService {

	/**
	 * 대분류 카테고리 목록을 조회합니다.
	 *
	 * @return 대분류 카테고리 엔티티 목록(List<CategoryEntity>). 각 CategoryEntity는 카테고리 PK와
	 *         이름(name)을 포함합니다.
	 */
	List<CategoryEntity> getCategoryEntities();

	/**
	 * 특정 대분류 카테고리에 속한 소분류 카테고리 목록을 조회합니다.
	 *
	 * @param categoryNo 조회할 대분류 카테고리의 PK
	 * @return 소분류 카테고리 목록(List<CategoryDTO>). 각 CategoryDTO는 소분류 카테고리
	 *         이름(categoryName)과 참조 테이블인 List<DetailCategoryDTO>를 포함합니다.
	 */
	List<CategoryDTO> getCategoryDetails(Long categoryNo);
}
