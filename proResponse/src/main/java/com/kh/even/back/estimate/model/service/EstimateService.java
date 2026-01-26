package com.kh.even.back.estimate.model.service;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.kh.even.back.auth.model.vo.CustomUserDetails;
import com.kh.even.back.estimate.model.dto.EstimateRequestDTO;
import com.kh.even.back.estimate.model.dto.EstimateRequestDetailDTO;
import com.kh.even.back.estimate.model.dto.EstimateRequestUpdateDTO;
import com.kh.even.back.estimate.model.dto.EstimateResponseDetailDTO;
import com.kh.even.back.estimate.model.dto.ExpertRequestUserDTO;
import com.kh.even.back.expert.model.dto.ExpertDTO;
import com.kh.even.back.expert.model.dto.ResponseEstimateDTO;
import com.kh.even.back.util.model.dto.PageResponse;

import jakarta.validation.Valid;

public interface EstimateService {

	/**
	 * 
	 * @param estimateReqeust   견적요청을 위한 값들을 담는 DTO
	 * @param files             견적 상세내용에 이미지들을 담는 List
	 * @param customUserDetails 현재 로그인한 유저의 정보
	 */
	void saveEstimate(EstimateRequestDTO estimateReqeust, List<MultipartFile> files,
			CustomUserDetails customUserDetails);

	/**
	 * @param customUserDetails 현재 로그인한 유저의 정보
	 * @param pageNo            현재 페이지 넘버
	 * @return PageResponse<ExpertDTO> 회원이 견적 요청했던 전문가 목록과 페이지 정보 리턴
	 */
	PageResponse<ExpertDTO> getMyEstimate(int pageNo, CustomUserDetails customUserDetails);

	/**
	 * 
	 * @param pageNo            앞에서 넘겨주는 페이지 넘버
	 * @param customUserDetails 인증유저
	 * @return 받은 견적 요청 리스트
	 */
	PageResponse<ResponseEstimateDTO> getReceivedEstimates(int pageNo, CustomUserDetails customUserDetails);

	/**
	 * 
	 * @param pageNo            앞에서 넘겨주는 페이지 넘버
	 * @param customUserDetails 인증유저
	 * @return 받은 요청 리스트
	 */
	PageResponse<ExpertRequestUserDTO> getReceivedRequests(int pageNo, CustomUserDetails customUserDetails);

	/**
	 * 
	 * @param customUserDetails 로그인한 전문가
	 * @return 견적 요청의 상세
	 */
	EstimateRequestDetailDTO getEstimateDetail(CustomUserDetails customUserDetails, Long requestNo);

	/**
	 * @param requestNo         견적 번호
	 * @param customUserDetails 로그인한 회원
	 * 
	 */
	void updateEstimateStatus(Long requestNo, CustomUserDetails customUserDetails);

	/**
	 * 
	 * @param customUserDetails 로그인한 유저
	 * @param requestNo         견적 번호
	 * @return 응답받은 견적의 상세 내용
	 */
	EstimateResponseDetailDTO getReceivedEstimateDetail(CustomUserDetails customUserDetails, Long requestNo);

	void updateRequestEstimate(Long requestNo, EstimateRequestUpdateDTO dto, List<MultipartFile> images,
			CustomUserDetails user);
	
	
	
}
