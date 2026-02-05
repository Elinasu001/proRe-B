package com.kh.even.back.category.model.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.kh.even.back.auth.model.vo.CustomUserDetails;
import com.kh.even.back.category.model.dto.CategoryDTO;
import com.kh.even.back.category.model.dto.ExpertListDTO;
import com.kh.even.back.category.model.entity.CategoryEntity;
import com.kh.even.back.category.model.mapper.CategoryMapper;
import com.kh.even.back.category.model.repository.CategoryRepository;
import com.kh.even.back.common.validator.AssertUtil;
import com.kh.even.back.util.PageInfo;
import com.kh.even.back.util.Pagenation;
import com.kh.even.back.util.PagingExecutor;
import com.kh.even.back.util.model.dto.PageResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {

	private final CategoryRepository categoryRepository;
	private final CategoryMapper categoryMapper;
	private final PagingExecutor pagingExecutor;

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

	@Override
	public PageResponse<ExpertListDTO> getExpertList(Long categoryDetailNo, int pageNo, CustomUserDetails userDetails) {

		  	Long userNo = null;
		  	
		  	

		    int listCount = categoryMapper.getExpertListCount(categoryDetailNo);

		    return pagingExecutor.execute(
		            pageNo,
		            6,
		            listCount,
		            "해당 카테고리의 전문가 조회를 실패했습니다.",
		            params -> {
		                params.put("userNo", userNo);
		                params.put("categoryDetailNo", categoryDetailNo);
		            },
		            categoryMapper::getExpertList
		    );
	}


}
