package com.kh.even.back.estimate.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kh.even.back.auth.model.vo.CustomUserDetails;
import com.kh.even.back.common.ResponseData;
import com.kh.even.back.estimate.model.dto.EstimateRequestDTO;
import com.kh.even.back.estimate.model.dto.EstimateRequestDetailDTO;
import com.kh.even.back.estimate.model.dto.EstimateRequestUpdateDTO;
import com.kh.even.back.estimate.model.dto.EstimateResponseDetailDTO;
import com.kh.even.back.estimate.model.dto.ExpertRequestUserDTO;
import com.kh.even.back.estimate.model.service.EstimateService;
import com.kh.even.back.expert.model.dto.ExpertDTO;
import com.kh.even.back.expert.model.dto.ResponseEstimateDTO;
import com.kh.even.back.util.model.dto.PageResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api/estimate")
@RequiredArgsConstructor
public class EstimateController {

	private final EstimateService estimateService;

	// 회원 -> 내 견적 요청 조회
	@GetMapping
	public ResponseEntity<ResponseData<PageResponse<ExpertDTO>>> getEstimate(
			@AuthenticationPrincipal CustomUserDetails customUserDetails,
			@RequestParam(name = "pageNo", defaultValue = "1") int pageNo) {

		return ResponseData.ok(estimateService.getMyEstimate(pageNo, customUserDetails), "조회에 성공 했습니다.");

	}

	// 회원 -> 전문가에게 견적 응답 조회
	@GetMapping("/receive")
	public ResponseEntity<ResponseData<PageResponse<ResponseEstimateDTO>>> getReceivedEstimates(
			@AuthenticationPrincipal CustomUserDetails customUserDetails,
			@RequestParam(name = "pageNo", defaultValue = "1") int pageNo) {

		return ResponseData.ok(estimateService.getReceivedEstimates(pageNo, customUserDetails), "조회에 성공 했습니다.");

	}

	// 회원 -> 전문가 견적 상세
	@GetMapping("response/{requestNo}")
	public ResponseEntity<ResponseData<EstimateResponseDetailDTO>> getReceivedEstimateDetail(
			@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable("requestNo") Long requestNo) {

		EstimateResponseDetailDTO dto = estimateService.getReceivedEstimateDetail(customUserDetails, requestNo);

		return ResponseData.ok(dto, "조회에 성공 했습니다.");

	}

	// 전문가 견적 요청 자세히 보기
	@GetMapping("/request/{requestNo}")
	public ResponseEntity<ResponseData<EstimateRequestDetailDTO>> getEsimateDetail(
			@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable("requestNo") Long requestNo) {

		return ResponseData.ok(estimateService.getEstimateDetail(userDetails, requestNo), "조회에 성공 했습니다.");

	}

	// 전문가 회원 -> 전문가 견적 요청 조회
	@GetMapping("/requests")
	public ResponseEntity<ResponseData<PageResponse<ExpertRequestUserDTO>>> getReceivedRequests(
			@AuthenticationPrincipal CustomUserDetails userDetails,
			@RequestParam(name = "pageNo", defaultValue = "1") int pageNo) {

		return ResponseData.ok(estimateService.getReceivedRequests(pageNo, userDetails), "조회에 성공 했습니다.");

	}

	// 회원 -> 전문가 견적 요청
	@PostMapping
	public ResponseEntity<ResponseData<Void>> saveEstimate(@Valid @ModelAttribute EstimateRequestDTO estimateRequest,
			@RequestParam(value = "images", required = false) List<MultipartFile> images,
			@AuthenticationPrincipal CustomUserDetails customUserDetails) {

		estimateService.saveEstimate(estimateRequest, images, customUserDetails);

		return ResponseData.created(null, "견적 요청에 성공했습니다.");

	}

	// 회원 -> 전문가 견적 승낙
	@PutMapping("/{requestNo}/accept")
	public ResponseEntity<ResponseData<Void>> updateEstimateStatus(@PathVariable("requestNo") Long requestNo,
			@AuthenticationPrincipal CustomUserDetails customUserDetails) {

		estimateService.updateEstimateStatus(requestNo, customUserDetails);

		return ResponseData.ok(null, "해당 견적 승낙에 성공하였습니다.");

	}

	// 회원 견적 요청 수정
	@PutMapping("/{requestNo}")
	public ResponseEntity<ResponseData<Void>> updateRequestEstimate(
	        @AuthenticationPrincipal CustomUserDetails user,
	        @PathVariable("requestNo") Long requestNo,
	        @ModelAttribute @Valid EstimateRequestUpdateDTO dto,
	        @RequestParam(value = "images", required = false) List<MultipartFile> images
			){
	
		
			
		 estimateService.updateRequestEstimate(requestNo, dto, images, user);

		 return ResponseData.ok(null, "견적 요청 수정에 성공했습니다.");

	}

}
