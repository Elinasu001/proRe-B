package com.kh.even.back.estimate.model.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.kh.even.back.estimate.model.dto.EstimateReqeustDTO;

public interface EstimateService {

	
	/**
	 * 
	 * @param estimateReqeust 견적요청을 위한 값들을 담는 DTO
	 * @param files 견적 상세내용에 이미지들을 담는 List
	 */
	void saveEstimate(EstimateReqeustDTO estimateReqeust, List<MultipartFile> files);
	
}
