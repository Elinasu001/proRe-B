package com.kh.even.back.estimate.model.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.kh.even.back.auth.model.vo.CustomUserDetails;
import com.kh.even.back.estimate.model.dto.EstimateRequestDTO;
import com.kh.even.back.expert.model.dto.ExpertDTO;

public interface EstimateService {

	/**
	 * 
	 * @param estimateReqeust 견적요청을 위한 값들을 담는 DTO
	 * @param files           견적 상세내용에 이미지들을 담는 List
	 * @param customUserDetails 현재 로그인한 유저의 정보
	 */
	void saveEstimate(EstimateRequestDTO estimateReqeust, List<MultipartFile> files , CustomUserDetails customUserDetails);

	/**
	 * @param customUserDetails 현재 로그인한 유저의 정보
	 * @param page 현재 페이지 넘버 
	 * @return List<ExpertDTO> 회원이 견적 요청했던 목록 리턴
	 */
	List<ExpertDTO> getEstimate(CustomUserDetails customUserDetails,int page);
}
