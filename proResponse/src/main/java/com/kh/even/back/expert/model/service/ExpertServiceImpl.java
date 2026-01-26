package com.kh.even.back.expert.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.kh.even.back.auth.model.vo.CustomUserDetails;
import com.kh.even.back.common.validator.AssertUtil;
import com.kh.even.back.estimate.model.Entity.EstimateRequestEntity;
import com.kh.even.back.estimate.model.repository.EstimateRepository;
import com.kh.even.back.estimate.model.status.EstimateRequestStatus;
import com.kh.even.back.exception.CustomAuthorizationException;
import com.kh.even.back.exception.EntityNotFoundException;
import com.kh.even.back.exception.NotFoundException;
import com.kh.even.back.expert.model.dto.CategoryResponseDTO;
import com.kh.even.back.expert.model.dto.ExpertDetailDTO;
import com.kh.even.back.expert.model.dto.ExpertEstimateDTO;
import com.kh.even.back.expert.model.dto.LargeCategoryDTO;
import com.kh.even.back.expert.model.dto.MiddleCategoryDTO;
import com.kh.even.back.expert.model.dto.SmallCategoryDTO;
import com.kh.even.back.expert.model.entity.ExpertEstimateEntity;
import com.kh.even.back.expert.model.mapper.ExpertMapper;
import com.kh.even.back.expert.model.repository.ExpertEstimateRepository;
import com.kh.even.back.expert.model.repository.ExpertRepository;
import com.kh.even.back.expert.model.vo.LargeCategoryVO;
import com.kh.even.back.expert.model.vo.MiddleCategoryVO;
import com.kh.even.back.expert.model.vo.SmallCategoryVO;
import com.kh.even.back.file.service.FileUploadService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExpertServiceImpl implements ExpertService {

	private final FileUploadService fileUploadService;
	private final ExpertMapper mapper;
	private final ExpertRepository repository;
	private final ExpertEstimateRepository expertEstimateRepository;
	private final EstimateRepository estimateRepository;

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

		EstimateRequestEntity requestEntity = estimateRepository.findById(entity.getRequestNo())
				.orElseThrow(() -> new EntityNotFoundException("유효하지 않은 요청 번호입니다."));

		// JPA의 dirtyCheck
		requestEntity.changeStatus(EstimateRequestStatus.QUOTED);

		fileUploadService.uploadFiles(files, "expertEstimate", entity.getEstimateNo(),
				mapper::saveExpertEstimateAttachment);

	}

	private ExpertEstimateEntity toEntity(ExpertEstimateDTO expertEstimateDTO) {

		return ExpertEstimateEntity.builder().requestNo(expertEstimateDTO.getRequestNo())
				.price(expertEstimateDTO.getPrice()).content(expertEstimateDTO.getContent()).build();

	}
	
	/**
	 * 전문가 등록을 위해 카테고리를 조회하는 기능
	 */
	public CategoryResponseDTO getExpertCategory(CustomUserDetails user) {
		
		// 이미 전문가인 경우에는 전문가 등록에 접근하지 못한다.
		boolean isExpert = user.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_EXPERT"));
		if(isExpert) {
			throw new CustomAuthorizationException("이미 전문가인 회원입니다.");
		}
		
		List<LargeCategoryVO> large = mapper.getLargeCategory();
		List<MiddleCategoryVO> middle = mapper.getMiddleCategory();
		List<SmallCategoryVO> small = mapper.getSmallCategory();
		
		// 조회해온 VO 유효성 검사
		validateCategories(large, middle, small);
		
		// VO -> DTO로 가공한 대분류 리스트 반환
		List<LargeCategoryDTO> categories = buildCategoryTree(large, middle, small);
		
		return new CategoryResponseDTO(categories);
	}
	
	/**
	 * 전문가 등록을 위한 대/중/소 카테고리의 유효성을 검사합니다.
	 * @param lists
	 */
	private void validateCategories(List<?>...lists) {
		
		for(List<?> list : lists) {
			if(list == null || list.isEmpty()) {
				throw new NotFoundException("카테고리 조회에 실패했습니다.");
			}
		}
		
	}
	
	/**
	 * 컨트롤러에 반환할 대/중/소 카테고리 리스트를 VO -> DTO로 가공하는 용도
	 * @param large
	 * @param middle
	 * @param small
	 * @return 중분류/소분류가 포함된 대분류 DTO
	 */
	private List<LargeCategoryDTO> buildCategoryTree(List<LargeCategoryVO> large, List<MiddleCategoryVO> middle, List<SmallCategoryVO> small) {
	
		// 최종적으로 리턴할 카테고리 리스트 선언
		List<LargeCategoryDTO> categories = new ArrayList();
		
		// 대분류 가공
		for(LargeCategoryVO largeList : large) {
			LargeCategoryDTO largeDTO = new LargeCategoryDTO();
			largeDTO.setExpertTypeNo(largeList.getExpertTypeNo());
			largeDTO.setExpertName(largeList.getExpertName());
			largeDTO.setCategories(new ArrayList<>());
		
		
		// 중분류 가공
		for(MiddleCategoryVO middleList : middle) {
			
			if(!largeList.getExpertTypeNo().equals(middleList.getExpertTypeNo())) {
				continue;
			}
			
			MiddleCategoryDTO middleDTO = new MiddleCategoryDTO();
			middleDTO.setCategoryNo(middleList.getCategoryNo());
			middleDTO.setCategoryName(middleList.getCategoryName());
			middleDTO.setCategories(new ArrayList<>());
		
		
		// 소분류 가공
		for(SmallCategoryVO smallList : small) {
			
			if(!middleList.getCategoryNo().equals(smallList.getCategoryNo())) {
				continue;
			}
			
			SmallCategoryDTO smallDTO = new SmallCategoryDTO();
			smallDTO.setCategoryDetailNo(smallList.getCategoryDetailNo());
			smallDTO.setCategoryDetailName(smallList.getCategoryDetailName());
			
			middleDTO.getCategories().add(smallDTO);
		}
		
		  largeDTO.getCategories().add(middleDTO);
		
		}
		
		 categories.add(largeDTO);
		
		}
		
		return categories;
	}
	
}
