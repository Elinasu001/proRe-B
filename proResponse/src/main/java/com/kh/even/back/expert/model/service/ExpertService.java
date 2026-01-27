package com.kh.even.back.expert.model.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.kh.even.back.auth.model.vo.CustomUserDetails;
import com.kh.even.back.category.model.dto.DetailCategoryDTO;
import com.kh.even.back.category.model.dto.ExpertListDTO;
import com.kh.even.back.estimate.model.dto.ExpertRequestUserDTO;
import com.kh.even.back.expert.model.dto.ExpertDetailDTO;
import com.kh.even.back.expert.model.dto.ExpertEstimateDTO;
import com.kh.even.back.expert.model.dto.ExpertLocationDTO;
import com.kh.even.back.expert.model.dto.ExpertSearchDTO;
import com.kh.even.back.expert.model.dto.LargeCategoryDTO;
import com.kh.even.back.util.model.dto.PageResponse;

public interface ExpertService {

	/**
	 * 
	 * @param expertNo 앞단에서 가져오는 전문가 번호
	 * @param user     로그인 유저
	 * @return 전문가 자세히보기를 눌렀을때 나오는 정보를 담은 DTO
	 */
	ExpertDetailDTO getExpertDetails(Long expertNo, CustomUserDetails user);

	/**
	 * 
	 * @param expertEstimate 전문가가 견적 응답을 하는 값을 담는 DTO
	 * @param images         상세 이미지들
	 * @param user           로그인한 전문가 user
	 */
	void saveEstimate(ExpertEstimateDTO expertEstimate, List<MultipartFile> images, CustomUserDetails user);

	/**
	 * 
	 * @param pageNo 페이징 처리
	 * @param user   로그인한 전문가
	 * @return 매칭된 유저들
	 */
	PageResponse<ExpertRequestUserDTO> getMatchedUser(int pageNo, CustomUserDetails user);

	/**
	 * 
	 * @param expertNo 전문가의 번호
	 * @return 전문가가 가지고 있는 카테고리들
	 */
	List<DetailCategoryDTO> getExpertCategories(Long expertNo);

	/**
	 * 
	 * @param latitude  현재 유저의 위도
	 * @param longitude 현재 유저의 경도
	 * @param radius    선택한 범위
	 * @return 범위 안에 전문가들
	 */
	List<ExpertLocationDTO> getExpertMapLocations(double latitude, double longitude, int radius);
	
	/**
	 * 
	 * @param user 로그인한 회원
	 * @param pageNo 페이징 처리
	 * @return 좋아요한 전문가들
	 */
	PageResponse<ExpertListDTO> getLikedExperts(CustomUserDetails user , int pageNo);
	
	/**
	 * 
	 * @param requestNo 
	 * @param user
	 */
	void deleteExpertEstimateByRequestNo(Long requestNo , CustomUserDetails user);
	
	
	PageResponse<ExpertSearchDTO> getExpertsByNickname(String keyword , int pageNo);
	
	/**
	 * 전문가 등록을 위한 카테고리들을 조회합니다.
	 * @param user 회원정보
	 * @return 대/중/소분류 카테고리들을 가공한 DTO
	 */
	List<LargeCategoryDTO> getExpertCategory(CustomUserDetails user);
}
