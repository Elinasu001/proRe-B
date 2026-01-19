package com.kh.even.back.estimate.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kh.even.back.common.ResponseData;
import com.kh.even.back.estimate.model.dto.EstimateReqeustDTO;
import com.kh.even.back.estimate.model.service.EstimateService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api/estimate")
@RequiredArgsConstructor
public class EstimateController {

	private final EstimateService estimateService;
	
	@PostMapping
	public ResponseEntity<ResponseData<Void>> saveEstimate(@ModelAttribute EstimateReqeustDTO estimateRequest , List<MultipartFile> files){
		
		estimateService.save(estimateRequest , files );
		
		return ResponseData.created(null, "견적 요청에 성공했습니다.");
		
		
	}
}
