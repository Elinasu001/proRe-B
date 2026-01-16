package com.kh.even.back.category.model.service;

import java.util.List;

import com.kh.even.back.category.model.dto.CategoryDTO;
import com.kh.even.back.category.model.entity.CategoryEntity;

public interface CategoryService {

	List<CategoryEntity> getCategoryEntities();

	List<CategoryDTO> getCategoryDetails(Long categoryNo);
}
