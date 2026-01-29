package com.kh.even.back.expert.model.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.kh.even.back.auth.model.vo.CustomUserDetails;
import com.kh.even.back.category.model.dto.DetailCategoryDTO;
import com.kh.even.back.category.model.dto.ExpertListDTO;
import com.kh.even.back.common.validator.AssertUtil;
import com.kh.even.back.estimate.model.Entity.EstimateRequestEntity;
import com.kh.even.back.estimate.model.dto.ExpertRequestUserDTO;
import com.kh.even.back.estimate.model.mapper.EstimateMapper;
import com.kh.even.back.estimate.model.repository.EstimateRepository;
import com.kh.even.back.estimate.model.status.EstimateRequestStatus;
import com.kh.even.back.exception.CustomAuthorizationException;
import com.kh.even.back.exception.EntityNotFoundException;
import com.kh.even.back.exception.ExpertNotFoundException;
import com.kh.even.back.exception.ExpertRegisterException;
import com.kh.even.back.exception.NotFoundException;
import com.kh.even.back.exception.UpdateMemberException;
import com.kh.even.back.expert.model.dto.ExpertDetailDTO;
import com.kh.even.back.expert.model.dto.ExpertEstimateDTO;
import com.kh.even.back.expert.model.dto.ExpertLocationDTO;
import com.kh.even.back.expert.model.dto.ExpertRegisterDTO;
import com.kh.even.back.expert.model.dto.ExpertSearchDTO;
import com.kh.even.back.expert.model.dto.LargeCategoryDTO;
import com.kh.even.back.expert.model.dto.RegisterResponseDTO;
import com.kh.even.back.expert.model.entity.ExpertEstimateEntity;
import com.kh.even.back.expert.model.mapper.ExpertMapper;
import com.kh.even.back.expert.model.repository.ExpertEstimateRepository;
import com.kh.even.back.expert.model.repository.ExpertRepository;
import com.kh.even.back.expert.model.status.EstimateResponseStatus;
import com.kh.even.back.expert.model.vo.ExpertRegisterVO;
import com.kh.even.back.file.service.FileUploadService;
import com.kh.even.back.file.service.S3Service;
import com.kh.even.back.util.PageInfo;
import com.kh.even.back.util.Pagenation;
import com.kh.even.back.util.model.dto.PageResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExpertServiceImpl implements ExpertService {

	private final FileUploadService fileUploadService;
	private final ExpertMapper mapper;
	private final EstimateMapper estimateMapper;
	private final ExpertRepository repository;
	private final ExpertEstimateRepository expertEstimateRepository;
	private final EstimateRepository estimateRepository;
	private final Pagenation pagenation;
	private final S3Service s3Service;

	public ExpertDetailDTO getExpertDetails(Long expertNo, CustomUserDetails user) {

		Long userNo = null;

		if (user != null) {
			userNo = user.getUserNo();
		}

		validateExpertCount(expertNo);

		Map<String, Long> param = new HashMap();

		param.put("expertNo", expertNo);

		param.put("userNo", userNo);

		ExpertDetailDTO expertDetail = mapper.getExpertDetails(param);

		// log.info("expertDetail 체크 : {} " , expertDetail);

		return expertDetail;
	}

	private void validateExpertCount(Long expertNo) {

		int count = repository.countByUserNo(expertNo);

		// log.info(" JPA로 count 잘 들어오는지 확인 : {} " , count);

		AssertUtil.notFound(count, "유효하지 않은 전문가 번호입니다.");

	}

	@Override
	@Transactional
	public void saveEstimate(ExpertEstimateDTO expertEstimate, List<MultipartFile> files, CustomUserDetails user) {

		Long userNo = user.getUserNo();

		int count = mapper.countByRequestNoAndUserNo(expertEstimate.getRequestNo(), userNo);

		// log.info(" count 값 확인 info : {} " , count);

		AssertUtil.notFound(count, "해당 견적 요청이 존재하지 않거나 접근 권한이 없습니다.");

		AssertUtil.validateImageFiles(files);

		ExpertEstimateEntity entity = expertEstimateRepository.save(toEntity(expertEstimate));
		expertEstimateRepository.flush();

		EstimateRequestEntity requestEntity = estimateRepository.findById(entity.getRequestNo())
				.orElseThrow(() -> new EntityNotFoundException("유효하지 않은 요청 번호입니다."));

		log.info("entity : {} ", entity);

		// JPA의 dirtyCheck
		requestEntity.changeStatus(EstimateRequestStatus.QUOTED);

		fileUploadService.uploadFiles(files, "expertEstimate", entity.getEstimateNo(),
				mapper::saveExpertEstimateAttachment);

	}

	private ExpertEstimateEntity toEntity(ExpertEstimateDTO expertEstimateDTO) {

		return ExpertEstimateEntity.builder().requestNo(expertEstimateDTO.getRequestNo())
				.price(expertEstimateDTO.getPrice()).content(expertEstimateDTO.getContent())
				.status(EstimateResponseStatus.SENT).build();

	}

	@Override
	public PageResponse<ExpertRequestUserDTO> getMatchedUser(int pageNo, CustomUserDetails user) {

		Long userNo = user.getUserNo();

		int listCount = mapper.countMatchedByUserNo(userNo);

		AssertUtil.notFound(listCount, "받은 견적 내역이 없습니다.");

		Map<String, Object> params = pagenation.pageRequest(pageNo, 4, listCount);

		params.put("userNo", userNo);

		List<ExpertRequestUserDTO> list = mapper.getMatchedUser(params);

		PageInfo pageInfo = (PageInfo) params.get("pi");

		return new PageResponse<ExpertRequestUserDTO>(list, pageInfo);
	}

	@Override
	public List<DetailCategoryDTO> getExpertCategories(Long expertNo) {

		// List<DetailCategoryDTO> list = mapper.getExpertCategories(expertNo);

		// log.info(" List<DetailCategoryDTO> list 확인 info : {} " , list );

		return mapper.getExpertCategories(expertNo);

	}

	@Override
	public List<ExpertLocationDTO> getExpertMapLocations(double latitude, double longitude, int radius) {

		List<ExpertLocationDTO> list = mapper.getExpertLocations();

		double userLat = latitude;
		double userLng = longitude;

		return list.stream().filter(e -> e.getLatitude() != null && e.getLongitude() != null)
				.filter(e -> e.getLatitude() >= -90 && e.getLatitude() <= 90 && e.getLongitude() >= -180
						&& e.getLongitude() <= 180)
				.filter(e -> distance(userLat, userLng, e.getLatitude(), e.getLongitude()) <= radius).toList();
	}

	private double distance(double userLat, double userLng, double expertLat, double expertLon) {
		double r = 6371;
		double dLat = Math.toRadians(expertLat - userLat);
		double dLon = Math.toRadians(expertLon - userLng);

		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(userLat))
				* Math.cos(Math.toRadians(expertLat)) * Math.sin(dLon / 2) * Math.sin(dLon / 2);

		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		return r * c;
	}

	@Override
	public PageResponse<ExpertListDTO> getLikedExperts(CustomUserDetails user, int pageNo) {

		Long userNo = user.getUserNo();

		int listCount = mapper.getLikedExpertsCount(userNo);

		AssertUtil.notFound(listCount, "찜한 전문가가 없습니다.");

		Map<String, Object> params = pagenation.pageRequest(pageNo, 6, listCount);

		params.put("userNo", userNo);

		List<ExpertListDTO> list = mapper.getLikedExperts(params);

		PageInfo pageInfo = (PageInfo) params.get("pi");

		return new PageResponse<ExpertListDTO>(list, pageInfo);

	}

	@Override
	@Transactional
	public void deleteExpertEstimateByRequestNo(Long requestNo, CustomUserDetails user) {

		Long userNo = user.getUserNo();

		AssertUtil.notFound(mapper.countByRequestNoAndUserNo(requestNo, userNo), "회원의 견적이 아니거나 견적을 찾을수가 없습니다.");

		List<String> responseFiles = mapper.findAllbyRequestNo(requestNo);
		List<String> requestFiles = estimateMapper.findAllbyRequestNo(requestNo);

		mapper.softDeleteAllAttachments(requestNo);
		mapper.updateStatusByRequestNo(requestNo);

		estimateMapper.softDeleteAllAttachments(requestNo);
		estimateMapper.updateStatusByRequestNo(requestNo);

		responseFiles.forEach(s3Service::deleteFile);
		requestFiles.forEach(s3Service::deleteFile);

	}

	@Override
	public PageResponse<ExpertSearchDTO> getExpertsByNickname(String keyword, int pageNo) {

		int listCount = mapper.countExpertsByKeyword(keyword);

		AssertUtil.notFound(listCount, "키워드에 해당하는 전문가를 조회할 수 없습니다.");

		Map<String, Object> params = pagenation.pageRequest(pageNo, 10, listCount);

		params.put("keyword", keyword);

		List<ExpertSearchDTO> list = mapper.getExpertsByNickname(params);

		PageInfo pageInfo = (PageInfo) params.get("pi");

		return new PageResponse<ExpertSearchDTO>(list, pageInfo);
	}
	
	/**
	 * 전문가 등록을 위해 카테고리를 조회하는 기능
	 */
	public List<LargeCategoryDTO> getExpertCategory(CustomUserDetails user) {
		
		// 이미 전문가인 경우에는 전문가 등록에 접근하지 못한다.
		isExpert(user);
		
		List<LargeCategoryDTO> categories = mapper.getExpertCategory();
		if(categories == null || categories.isEmpty()) {
			throw new NotFoundException("카테고리 조회에 실패했습니다.");
		}
		
		return categories;
	}
	
	/**
	 * 전문가 등록을 하는 기능
	 */
	@Transactional
	public RegisterResponseDTO registerExpert(ExpertRegisterDTO expert, List<MultipartFile> files, CustomUserDetails user) {

		// 이미 전문가인 경우에는 전문가 등록에 접근하지 못한다.
		isExpert(user);
		
		// TB_EXPERT에 INSERT할 VO 가공 -> 매퍼 호출
		ExpertRegisterVO registerVO = toExpertVO(expert, user.getUserNo());
		int result = mapper.insertExpert(registerVO);
		if(result <= 0) {
			throw new ExpertRegisterException("전문가 등록에 실패했습니다.");
		}
		Long refNo = registerVO.getUserNo();
		
		// TB_MEMBER USER_ROLE -> EXPERT 업데이트
		int updateRole = mapper.updateRoleToExpert(refNo);
		if(updateRole == 0) {
			throw new UpdateMemberException("권한 변경에 실패했습니다.");
		}
		
		// 사용자가 선택한 소분류 카테고리를 중복값 필터/유효성 검사 후 TB_EXPERT_SAVE_CATEGORY에 INSERT드
		insertExpertCategoryDetail(refNo, expert.getCategoryDetailNos());
		
		// 파일 유효성 검사
		List<MultipartFile> validFiles = filterValidFiles(files);
		// 파일 개수 및 형식 검사
		AssertUtil.validateImageFiles(validFiles);
		// 매퍼 호출
		if(!validFiles.isEmpty()) {
	       fileUploadService.uploadFiles(validFiles, "expertRegistration", refNo, mapper::insertExpertAttachment);
	    }
		
		// 응답용 DTO 요청 후 반환
		return getNewExpert(refNo);
		
	}
	
	/**
	 * 해당 회원이 전문가인지 권한을 검증합니다.
	 * @param user
	 */
	private void isExpert(CustomUserDetails user) {
		
		boolean isExpert = user.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_EXPERT"));
		if(isExpert) {
			throw new CustomAuthorizationException("이미 전문가인 회원입니다."); 
		}
	}
	
	/**
	 * 전문가 등록에 필요한 값을 VO로 가공합니다.
	 * @param expert 프론트에서 받아온 전문가 등록 입력값
	 * @param userNo 회원PK
	 * @return DB에 INSERT할 전문가 등록 VO
	 */
	private ExpertRegisterVO toExpertVO(ExpertRegisterDTO expert, Long userNo) {
		
		return ExpertRegisterVO.builder().userNo(userNo)
										 .career(expert.getCareer())
										 .startTime(expert.getStartTime())
										 .endTime(expert.getEndTime())
										 .content(expert.getContent())
										 .expertTypeNo(expert.getExpertTypeNo())
										 .build();
	}
	
	/**
	 * 리스트로 받아온 파일의 유효성 검사
	 * @param files 전문가 등록에 첨부된 파일
	 * @return 유효성 검사를 마친 파일
	 */
	private List<MultipartFile> filterValidFiles(List<MultipartFile> files) {
	    if (files == null) return List.of();
	    return files.stream()
	            .filter(file -> file != null && !file.isEmpty())
	            .toList();
	}
	
	/**
	 * 새로 등록한 전문가의 응답 데이터를 받아옵니다.
	 * @param userNo 회원PK
	 * @return 전문가를 조회해온 응답용 DTO
	 */
	private RegisterResponseDTO getNewExpert(Long userNo) {
		List<RegisterResponseDTO> rows = mapper.getNewExpert(userNo);
		RegisterResponseDTO dto =
		        rows.isEmpty() ? null : rows.get(0);
		if(dto == null) {
			throw new NotFoundException("해당 전문가 조회에 실패했습니다.");
		}
		return dto;
	}
	
	/**
	 * 전문가 수정폼을 위해 내정보를 조회합니다.
	 */
	public RegisterResponseDTO getExpertForEdit(CustomUserDetails user) {
		
		// 전문가만 접근 가능합니다.
		validateExpert(user);
		
		RegisterResponseDTO responseDTO = mapper.getExpertForEdit(user.getUserNo());
		if(responseDTO == null) {
			throw new ExpertNotFoundException("내정보 조회에 실패했습니다.");
		}
		
		return responseDTO;
		
	}
	
	/**
	 * 전문가 내정보 수정하기
	 */
	@Transactional
	public RegisterResponseDTO updateExpert(ExpertRegisterDTO request, List<Long> deleteFileNos, 
											List<MultipartFile> newFiles, CustomUserDetails user) {
		
		// 일반 회원은 전문가 정보 수정에 접근하지 못한다.
		validateExpert(user);
		
		// TB_EXPERT에 UPDATE할 VO 가공 -> 매퍼 호출
		ExpertRegisterVO registerVO = toExpertVO(request, user.getUserNo());
		int result = mapper.updateExpert(registerVO);
		if(result <= 0) {
			throw new ExpertRegisterException("전문가 정보 수정에 실패했습니다.");
		}
		Long refNo = registerVO.getUserNo();
		
		// TB_EXPERT_SAVE_CATEGORY 기존값 물리적 삭제(유니크 제약 이슈)
		int deleteCategory = mapper.deleteExpertCategoryDetail(refNo);
		if(deleteCategory == 0) {
			throw new ExpertRegisterException("전문가 정보 수정에 실패했습니다.");
		}
		
		// 사용자가 선택한 소분류 카테고리를 중복값 필터/유효성 검사 후 TB_EXPERT_SAVE_CATEGORY에 INSERT
		insertExpertCategoryDetail(refNo, request.getCategoryDetailNos());
		
		// 전문가에게 허용한 상세이미지 개수를 초과하게 될지 검증
		validateAttachmentLimit(refNo, deleteFileNos, newFiles);
	    
		// 상세이미지 삭제
		if (deleteFileNos != null && !deleteFileNos.isEmpty()) {
		    mapper.deleteExpertAttachments(refNo, deleteFileNos);
		}
		
		// 새로운 상세이미지 추가를 위한 유효성 검사
		List<MultipartFile> validFiles = filterValidFiles(newFiles);
		// 파일 개수 및 형식 검사
		AssertUtil.validateImageFiles(validFiles);
		// 매퍼 호출
		if(!validFiles.isEmpty()) {
		   fileUploadService.uploadFiles(validFiles, "expertRegistration", refNo, mapper::insertExpertAttachment);
	    }
		
		return getNewExpert(refNo);
	}
	
	/**
	 * 전문가 기능에 접근 권한이 있는지 검증합니다.
	 * @param user
	 */
	private void validateExpert(CustomUserDetails user) {
		boolean isExpert = user.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_EXPERT"));
		if(!isExpert) {
			throw new CustomAuthorizationException("전문가만 접근할 수 있습니다."); 
		}
	}
	
	/**
	 * 사용자가 선택한 소분류 카테고리의 중복값 필터 및 유효성 검사 후 DB에 INSERT하는 메서드
	 * @param userNo 회원PK
	 * @param categoryDetailNos 사용자가 선택한 소분류 카테고리(1~3개)
	 */
	private void insertExpertCategoryDetail (Long userNo, List<Long> categoryDetailNos) {
		// 사용자가 선택한 소분류 카테고리(1개~3개) 중복값을 HashSet으로 필터
		Set<Long> uniqueCategoryDetailNos = new HashSet<>(categoryDetailNos);
		
		// 유효한 소분류 카테고리를 TB_SAVE_CATEGORY에 INSERT
		for(Long categoryDetailNo : uniqueCategoryDetailNos) {
			int insertCategory = mapper.insertExpertCategoryDetail(userNo, categoryDetailNo);
			if(insertCategory <= 0) {
			    throw new ExpertRegisterException("소분류 카테고리 저장에 실패했습니다.");
			}
	    }
	}
	
	/**
	 * 전문가가 정보를 수정할 때 상세이미지가 4개를 초과하게 될지 검증합니다.
	 * @param userNo 회원PK
	 * @param deleteFileNos 삭제할 상세이미지 개수
	 * @param newFiles 새로 추가할 상세이미지 개수
	 */
	private void validateAttachmentLimit(Long userNo, List<Long> deleteFileNos, List<MultipartFile> newFiles) {
		// 현재 활성화 중인 상세이미지 개수
	    int currentActive = mapper.countActiveAttachments(userNo); // STATUS='N'
	    
	    // 삭제할 상세이미지 개수
	    int validDelete = 0;
	    if (deleteFileNos != null && !deleteFileNos.isEmpty()) {
	        validDelete = mapper.countDeletableAttachments(userNo, deleteFileNos); // STATUS='N' AND FILE_NO IN (...)
	    }
	    
	    // 새로 추가할 상세이미지 개수
	    int newCount = (newFiles == null) ? 0 : (int) newFiles.stream()
	        .filter(f -> f != null && !f.isEmpty())
	        .count();
	    
	    int expected = currentActive - validDelete + newCount;
	    if (expected > 4) {
	        throw new IllegalArgumentException("상세 이미지는 최대 4개까지 등록할 수 있습니다.");
	    }
	}

	
}
