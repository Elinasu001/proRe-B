package com.kh.even.back.expert.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kh.even.back.auth.model.vo.CustomUserDetails;
import com.kh.even.back.common.ResponseData;
import com.kh.even.back.expert.model.dto.ExpertDetailDTO;
import com.kh.even.back.expert.model.dto.ExpertEstimateDTO;
import com.kh.even.back.expert.model.service.ExpertService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/experts")
@RequiredArgsConstructor
@Slf4j
public class ExpertController {

	private final ExpertService expertService;

	@GetMapping("/{expertNo}")
	public ResponseEntity<ResponseData<ExpertDetailDTO>> getExpertDetails(@PathVariable("expertNo") Long expertNo,
			@AuthenticationPrincipal CustomUserDetails user) {

		ExpertDetailDTO expertDetails = expertService.getExpertDetails(expertNo, user);

		return ResponseData.ok(expertDetails, "조회에 성공했습니다.");

	}

	@PostMapping("/estimate")
	public ResponseEntity<ResponseData<Void>> saveEstimate(@Valid @ModelAttribute ExpertEstimateDTO expertEstimateDTO,
			@RequestParam(value = "images", required = false) List<MultipartFile> images,
			@AuthenticationPrincipal CustomUserDetails user) {

		expertService.saveEstimate(expertEstimateDTO, images, user);

		return ResponseData.created(null, "견적 응답에 성공했습니다.");

	}
	
	@GetMapping("/registration")
	public ResponseEntity<ResponseData<ExpertCategoryDTO>> getExpertCategory(@AuthenticationPricipal CustomUserDetails user) {
		
		ExpertCategoryDTO categories = expertService.getExpertCategory(user);
		
		return ResponseData.ok(categories, "전문가 등록 카테고리 조회가 완료되었습니다.");
	}

}
