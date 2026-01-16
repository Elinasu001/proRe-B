package com.kh.even.back.category.model.service;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.kh.even.back.category.model.dto.CategoryDTO;
import com.kh.even.back.category.model.entity.CategoryEntity;
import com.kh.even.back.category.model.mapper.CategoryMapper;
import com.kh.even.back.category.model.repository.CategoryRepository;
import com.kh.even.back.common.validator.AssertUtil;
import com.kh.even.back.exception.NotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {

	private final CategoryRepository categoryRepository;
	private final CategoryMapper categoryMapper;

	@Override
	public List<CategoryEntity> getCategoryEntities() {

		return categoryRepository.findAll(Sort.by("categoryNo"));

	}

	@Override
	public List<CategoryDTO> getCategoryDetails(Long categoryNo) {

		// log.info("{}" , expertTypeNo);'

		int count = categoryRepository.countByCategoryNo(categoryNo).intValue();

		AssertUtil.notFound(count, "카테고리 상세 정보 조회를 실패했습니다.");

		return categoryMapper.getCategoryDetails(categoryNo);
	}

}
