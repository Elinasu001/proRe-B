package com.kh.even.back.expert.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.kh.even.back.auth.model.vo.CustomUserDetails;
import com.kh.even.back.common.ResponseData;
import com.kh.even.back.expert.model.dto.ExpertDetailDTO;
import com.kh.even.back.expert.model.service.ExpertService;

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
	public ResponseEntity<ResponseData<Void>> saveEstimate(@ResponseBody EstimateResponseDTO
			@AuthenticationPrincipal CustomUserDetails user) {

		ExpertDetailDTO expertDetails = expertService.getExpertDetails(expertNo, user);

		return ResponseData.ok(expertDetails, "조회에 성공했습니다.");
		
	}

}
