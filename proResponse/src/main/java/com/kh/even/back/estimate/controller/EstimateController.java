package com.kh.even.back.estimate.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kh.even.back.auth.model.vo.CustomUserDetails;
import com.kh.even.back.common.ResponseData;
import com.kh.even.back.estimate.model.dto.EstimateRequestDTO;
import com.kh.even.back.estimate.model.service.EstimateService;
import com.kh.even.back.expert.model.dto.ExpertDTO;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api/estimate")
@RequiredArgsConstructor
public class EstimateController {

	private final EstimateService estimateService;

	@GetMapping("/me")
	public ResponseEntity<ResponseData<List<ExpertDTO>>> getEstimate(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestParam(defaultValue = "1") int page) {

		return ResponseData.ok(estimateService.getEstimate(customUserDetails,page), "조회에 성공했습니다.");
		
	}

	@PostMapping
	public ResponseEntity<ResponseData<Void>> saveEstimate(@Valid @ModelAttribute EstimateRequestDTO estimateRequest,
			@RequestParam(value = "images", required = false) List<MultipartFile> images,
			@AuthenticationPrincipal CustomUserDetails customUserDetails) {

		estimateService.saveEstimate(estimateRequest, images, customUserDetails);

		return ResponseData.created(null, "견적 요청에 성공했습니다.");

	}
}
