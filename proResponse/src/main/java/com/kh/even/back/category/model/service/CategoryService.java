package com.kh.even.back.category.model.service;

import java.util.List;

import com.kh.even.back.auth.model.vo.CustomUserDetails;
import com.kh.even.back.category.model.dto.CategoryDTO;
import com.kh.even.back.category.model.dto.ExpertListDTO;
import com.kh.even.back.category.model.entity.CategoryEntity;
import com.kh.even.back.util.model.dto.PageResponse;

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

	
	/**
	 * 카테고리 넘버로 카테고리에 속한 전문가를 조회합니다.
	 * @param categoryDetailNo 카테고리의 상세넘버
	 * @param pageNo   페이징 처리를 위한 pageNo
	 * @param customUserDetails 로그인 유저
	 * @return 전문가들을 담은 ExpertListDTO
	 */
	PageResponse<ExpertListDTO> getExpertList(Long categoryDetailNo, int pageNo, CustomUserDetails customUserDetails);
}
