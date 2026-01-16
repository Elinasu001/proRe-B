package com.kh.even.back.category.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.kh.even.back.category.model.dto.CategoryDTO;

@Mapper
public interface CategoryMapper {

	List<CategoryDTO> getCategoryDetails(Long expertTypeNo);
	
}
