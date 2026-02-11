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
import com.kh.even.back.expert.model.dto.ExpertRegisterDTO;
import com.kh.even.back.expert.model.dto.ExpertSearchDTO;
import com.kh.even.back.expert.model.dto.LargeCategoryDTO;
import com.kh.even.back.expert.model.dto.RegisterResponseDTO;
import com.kh.even.back.expert.model.dto.SwitchRoleResponseDTO;
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
	
	/**
	 * 전문가 등록을 요청합니다.
	 * @param expert 전문가 등록을 위한 입력값
	 * @param file 상세이미지 첨부
	 * @param user 회원정보
	 * @return 전문가 등록에 성공할 경우 내려줄 응답용 DTO
	 */
	RegisterResponseDTO registerExpert(ExpertRegisterDTO expert, List<MultipartFile> files, CustomUserDetails user);
	
	/**
	 * 내정보를 조회합니다.
	 * @param user 회원정보
	 * @return 전문가 내정보 조회 응답용 DTO
	 */
	RegisterResponseDTO getExpertForEdit(CustomUserDetails user);
	
	/**
	 * 전문가 정보를 수정합니다.
	 * @param request 전문가 정보 수정을 위한 입력값
	 * @param files 상세이미지 첨부
	 * @param user 회원정보
	 * @return 전문가 등록에 성공할 경우 내려줄 응답용 DTO
	 */
	RegisterResponseDTO updateExpert(ExpertRegisterDTO request, List<Long> deleteFileNos, List<MultipartFile> newFiles, CustomUserDetails user);
	
	/**
	 * 일반회원에서 전문가로 전환합니다. (전문가 이력이 있는 경우)
	 * @param user
	 * @return 전문가 전환 후 토큰과 권한을 재응답
	 */
	SwitchRoleResponseDTO switchToExpert(CustomUserDetails user);
	
	/**
	 * 전문가에서 일반회원으로 전환합니다.
	 * @param user
	 * @return 전문가 전환 후 토큰과 권한을 재응답
	 */
	SwitchRoleResponseDTO switchToUser(CustomUserDetails user);
}
