package com.kh.even.back.expert.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import com.kh.even.back.category.model.dto.DetailCategoryDTO;
import com.kh.even.back.category.model.dto.ExpertListDTO;
import com.kh.even.back.common.ResponseData;
import com.kh.even.back.estimate.model.dto.ExpertRequestUserDTO;
import com.kh.even.back.expert.model.dto.ExpertDetailDTO;
import com.kh.even.back.expert.model.dto.ExpertEstimateDTO;
import com.kh.even.back.expert.model.dto.ExpertLocationDTO;
import com.kh.even.back.expert.model.dto.ExpertRegisterDTO;
import com.kh.even.back.expert.model.dto.ExpertSearchDTO;
import com.kh.even.back.expert.model.dto.LargeCategoryDTO;
import com.kh.even.back.expert.model.dto.RegisterResponseDTO;
import com.kh.even.back.expert.model.service.ExpertService;
import com.kh.even.back.util.model.dto.PageResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/experts")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ExpertController {

	private final ExpertService expertService;

	// 전문가 상세 조회
	@GetMapping("/{expertNo}")
	public ResponseEntity<ResponseData<ExpertDetailDTO>> getExpertDetails(@PathVariable("expertNo") Long expertNo,
			@AuthenticationPrincipal CustomUserDetails user) {

		ExpertDetailDTO expertDetails = expertService.getExpertDetails(expertNo, user);

		return ResponseData.ok(expertDetails, "조회에 성공했습니다.");

	}

	// 전문가 -> 회원 견적 응답
	@PostMapping("/estimate")
	public ResponseEntity<ResponseData<Void>> saveEstimate(@Valid @ModelAttribute ExpertEstimateDTO expertEstimateDTO,
			@RequestParam(value = "images", required = false) List<MultipartFile> images,
			@AuthenticationPrincipal CustomUserDetails user) {

		expertService.saveEstimate(expertEstimateDTO, images, user);

		return ResponseData.created(null, "견적 응답에 성공했습니다.");

	}
	
	// 전문가 등록 카테고리 조회
	@GetMapping("/registration")
	public ResponseEntity<ResponseData<List<LargeCategoryDTO>>> getExpertCategory(@AuthenticationPrincipal CustomUserDetails user) {
		
		List<LargeCategoryDTO> categories = expertService.getExpertCategory(user);
		
		return ResponseData.ok(categories, "전문가 등록 카테고리 조회가 완료되었습니다.");
	}
	


	// 전문가 -> 매치 성공 회원 조회
	@GetMapping("/matches")
	public ResponseEntity<ResponseData<PageResponse<ExpertRequestUserDTO>>> getMatchedUser(
			@AuthenticationPrincipal CustomUserDetails user,
			@RequestParam(name = "pageNo", defaultValue = "1") int pageNo) {

		return ResponseData.ok(expertService.getMatchedUser(pageNo, user), "조회에 성공 했습니다");

	}

	// 전문가의 카테고리들 조회
	@GetMapping("/{expertNo}/categories")
	public ResponseEntity<ResponseData<List<DetailCategoryDTO>>> getExpertCategories(
			@AuthenticationPrincipal CustomUserDetails user, @PathVariable("expertNo") Long expertNo) {

		return ResponseData.ok(expertService.getExpertCategories(expertNo), "조회에 성공 했습니다.");

	}

	// 지도에 나타낼 전문가 조회
	@GetMapping("/map")
	public ResponseEntity<ResponseData<List<ExpertLocationDTO>>> getExpertMapLocations(
			@RequestParam(name = "latitude") double latitude, @RequestParam(name = "longitude") double longitude,
			@RequestParam(name = "radius", defaultValue = "3") int radius) {

		return ResponseData.ok(expertService.getExpertMapLocations(latitude, longitude, radius), "조회에 성공 했습니다.");

	}

	// 내가 찜한 전문가 조회
	@GetMapping("/likes")
	public ResponseEntity<ResponseData<PageResponse<ExpertListDTO>>> getLikedExperts(
			@AuthenticationPrincipal CustomUserDetails user,
			@RequestParam(name = "pageNo", defaultValue = "1") int pageNo) {

		return ResponseData.ok(expertService.getLikedExperts(user, pageNo), "조회에 성공 했습니다.");

	}

	// 전문가 견적 삭제
	@DeleteMapping("/estimate/{requestNo}")
	public ResponseEntity<ResponseData<Void>> deleteExpertEstimateByRequestNo(@PathVariable("requestNo") Long requestNo,
			@AuthenticationPrincipal CustomUserDetails user) {

		expertService.deleteExpertEstimateByRequestNo(requestNo, user);

		return ResponseData.ok(null, "견적 삭제에 성공 했습니다.");

	}

	// 검색창 전문가 검색
	@GetMapping("/search")
	public ResponseEntity<ResponseData<PageResponse<ExpertSearchDTO>>> getExpertsByNickname(
			@RequestParam(name = "keyword") String keyword,
			@RequestParam(name = "pageNo", defaultValue = "1") int pageNo) {

		return ResponseData.ok(expertService.getExpertsByNickname(keyword, pageNo), "조회에 성공 했습니다.");
		
	}
	
	@PostMapping("/registration")
	public ResponseEntity<ResponseData<RegisterResponseDTO>> registerExpert(@Valid @ModelAttribute ExpertRegisterDTO expert,
			                                                                @RequestParam(name = "attachment", required=false) List<MultipartFile> files,
			                                                                @AuthenticationPrincipal CustomUserDetails user) {
		// log.info("전문가 등록 진위여부 : expert = {}, file = {}, user = {}", expert, file, user);
		
		RegisterResponseDTO ResponseDTO = expertService.registerExpert(expert, files, user);
	
		
		return ResponseData.created(ResponseDTO, "전문가 등록이 완료되었습니다.");
	}
	
	@GetMapping("/me")
	public ResponseEntity<ResponseData<RegisterResponseDTO>> getExpertForEdit(@AuthenticationPrincipal CustomUserDetails user) {
		
		RegisterResponseDTO ResponseDTO = expertService.getExpertForEdit(user);
		
		return ResponseData.ok(ResponseDTO, "내정보 조회가 완료되었습니다.");
	}
	
	@PutMapping("/me")
	public ResponseEntity<ResponseData<RegisterResponseDTO>> updateExpert(@Valid @ModelAttribute ExpertRegisterDTO request,
																		  @RequestParam(value = "deleteFileNos", required = false) List<Long> deleteFileNos,
																		  @RequestParam(value = "attachment", required = false) List<MultipartFile> newFiles,
																		  @AuthenticationPrincipal CustomUserDetails user) {
		// log.info("전문가 수정 진위여부 : request = {}, files = {}, user = {}", request, files, user);
		
		RegisterResponseDTO ResponseDTO = expertService.updateExpert(request, deleteFileNos, newFiles, user);
		
		return ResponseData.ok(ResponseDTO, "전문가 정보수정이 완료되었습니다.");
	}
}
