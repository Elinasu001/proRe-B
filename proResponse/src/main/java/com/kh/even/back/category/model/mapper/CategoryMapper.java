package com.kh.even.back.category.model.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.kh.even.back.category.model.dto.CategoryDTO;
import com.kh.even.back.category.model.dto.ExpertListDTO;
import com.kh.even.back.util.model.dto.PageResponse;

@Mapper
public interface CategoryMapper {

	List<CategoryDTO> getCategoryDetails(Long expertTypeNo);
	
	int getExpertListCount(Long categoryDetailNo);
	
	List<ExpertListDTO> getExpertList(Map<String, Object> param);
}
