package com.kh.even.back.category.model.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kh.even.back.category.model.dto.CategoryDTO;
import com.kh.even.back.category.model.entity.CategoryEntity;
import com.kh.even.back.category.model.mapper.CategoryMapper;
import com.kh.even.back.category.model.repository.CategoryRepository;

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


		
		return categoryRepository.findAll();
	}

	@Override
	public List<CategoryDTO> getCategoryDetails(Long expertTypeNo) {
		
		//log.info("{}" , expertTypeNo);
		
		List<CategoryDTO> categoryDetails = categoryMapper.getCategoryDetails(expertTypeNo);
		
		return categoryDetails;
	}

}
