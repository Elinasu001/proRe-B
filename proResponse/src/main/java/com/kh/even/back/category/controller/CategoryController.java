package com.kh.even.back.category.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kh.even.back.auth.model.vo.CustomUserDetails;
import com.kh.even.back.category.model.dto.CategoryDTO;
import com.kh.even.back.category.model.dto.ExpertListDTO;
import com.kh.even.back.category.model.entity.CategoryEntity;
import com.kh.even.back.category.model.service.CategoryService;
import com.kh.even.back.common.ResponseData;
import com.kh.even.back.util.model.dto.PageResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Slf4j
public class CategoryController {

	private final CategoryService categorySerivce;

	@GetMapping
	public ResponseEntity<ResponseData<List<CategoryEntity>>> getEntities() {

		List<CategoryEntity> categories = categorySerivce.getCategoryEntities();

		// log.info(" controller : {}", categories);

		return ResponseData.ok(categories, "카테고리 조회에 성공했습니다.");

	}

	@GetMapping("/{categoryNo}")
	public ResponseEntity<ResponseData<List<CategoryDTO>>> getCategories(@PathVariable("categoryNo") Long categoryNo) {

		// log.info("{}",expertTypeNo);

		List<CategoryDTO> categoryDetails = categorySerivce.getCategoryDetails(categoryNo);

		return ResponseData.ok(categoryDetails, "조회에 성공했습니다.");
	}

    @GetMapping("/experts/{categoryDetailNo}")
	public ResponseEntity<ResponseData<PageResponse<ExpertListDTO>>> getExpertList(@PathVariable("categoryDetailNo") Long categoryDetailNo , @RequestParam(name = "pageNo", defaultValue = "1") int pageNo , @AuthenticationPrincipal CustomUserDetails customUserDetails){
    	
    	return ResponseData.ok(categorySerivce.getExpertList(categoryDetailNo , pageNo , customUserDetails),"전문가 조회를 성공했습니다.");
    	
    }

}
