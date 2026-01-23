package com.kh.even.back.expert.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.kh.even.back.auth.model.vo.CustomUserDetails;
import com.kh.even.back.category.model.dto.ExpertCategoryDTO;
import com.kh.even.back.common.validator.AssertUtil;
import com.kh.even.back.estimate.model.Entity.EstimateRequestEntity;
import com.kh.even.back.estimate.model.repository.EstimateRepository;
import com.kh.even.back.estimate.model.status.EstimateRequestStatus;
import com.kh.even.back.exception.EntityNotFoundException;
import com.kh.even.back.expert.model.dto.ExpertDetailDTO;
import com.kh.even.back.expert.model.dto.ExpertEstimateDTO;
import com.kh.even.back.expert.model.entity.ExpertEstimateEntity;
import com.kh.even.back.expert.model.mapper.ExpertMapper;
import com.kh.even.back.expert.model.repository.ExpertEstimateRepository;
import com.kh.even.back.expert.model.repository.ExpertRepository;
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
	
	
	public void/*ExpertCategoryDTO*/ getExpertCategory(CustomUserDetails user) {
		
		// 회원 역할 검증(전문가는 '전문가 등록 페이지'에 접근불가)
		log.info("이거 뭐야 : {}", user.getAuthorities());
		if("[ROLE_EXPERT]".equals(user.getAuthorities())) {
			
		}
		
	}

}
