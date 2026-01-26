package com.kh.even.back.expert.model.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.kh.even.back.auth.model.vo.CustomUserDetails;
import com.kh.even.back.expert.model.dto.CategoryResponseDTO;
import com.kh.even.back.expert.model.dto.ExpertDetailDTO;
import com.kh.even.back.expert.model.dto.ExpertEstimateDTO;

public interface ExpertService {

	/**
	 * 
	 * @param expertNo 앞단에서 가져오는 전문가 번호
	 * @param user 로그인 유저
	 * @return 전문가 자세히보기를 눌렀을때 나오는 정보를 담은 DTO
	 */
	ExpertDetailDTO getExpertDetails(Long expertNo, CustomUserDetails user);

	
	/**
	 * 
	 * @param expertEstimate 전문가가 견적 응답을 하는 값을 담는 DTO
	 * @param images 상세 이미지들 
	 * @param user 로그인한 전문가 user
	 */
	void saveEstimate(ExpertEstimateDTO expertEstimate, List<MultipartFile> images , CustomUserDetails user);
	
	/**
	 * 전문가 등록을 위한 카테고리들을 조회합니다.
	 * @param user 회원정보
	 * @return 대/중/소분류 카테고리들을 가공한 DTO
	 */
	CategoryResponseDTO getExpertCategory(CustomUserDetails user);
}
