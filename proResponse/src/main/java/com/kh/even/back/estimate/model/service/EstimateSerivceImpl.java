package com.kh.even.back.estimate.model.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.kh.even.back.auth.model.vo.CustomUserDetails;
import com.kh.even.back.common.validator.AssertUtil;
import com.kh.even.back.estimate.model.Entity.EstimateRequestEntity;
import com.kh.even.back.estimate.model.dto.EstimateRequestDTO;
import com.kh.even.back.estimate.model.mapper.EstimateMapper;
import com.kh.even.back.estimate.model.repository.EstimateRepository;
import com.kh.even.back.expert.model.dto.ExpertDTO;
import com.kh.even.back.expert.model.dto.ResponseEstimateDTO;
import com.kh.even.back.file.model.vo.FileVO;
import com.kh.even.back.file.service.S3Service;
import com.kh.even.back.util.PageInfo;
import com.kh.even.back.util.Pagenation;
import com.kh.even.back.util.model.dto.PageResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EstimateSerivceImpl implements EstimateService {

	private final S3Service s3Service;
	private final EstimateMapper mapper;
	private final EstimateRepository repository;
	private final Pagenation pagenation;

	@Override
	@Transactional
	public void saveEstimate(EstimateRequestDTO estimateReqeust, List<MultipartFile> files,
			CustomUserDetails customUserDetails) {

		AssertUtil.checkFileSize(files);

		EstimateRequestEntity entity = toEntity(estimateReqeust, customUserDetails);

		EstimateRequestEntity savedEntity = repository.save(entity);

		if (files != null && !files.isEmpty()) {

			for (MultipartFile file : files) {

				String filePath = s3Service.store(file, "estimate");

				FileVO VO = FileVO.builder().originName(file.getOriginalFilename()).filePath(filePath)
						.reqNo(savedEntity.getRequestNo()).build();

				mapper.saveEstimateAttachment(VO);

			}

		}

	}

	// EstimateRequesdto -> entity
	private EstimateRequestEntity toEntity(EstimateRequestDTO dto, CustomUserDetails customUserDetails) {

		EstimateRequestEntity entity = EstimateRequestEntity.builder().requestDate(dto.getRequestDate())
				.requestType(dto.getRequestType()).requestService(dto.getRequestService()).content(dto.getContent())
				.expertNo(dto.getExpertNo()).categoryDetailNo(dto.getCategoryDetailNo())
				.userNo(customUserDetails.getUserNo()).build();

		return entity;
	}

	@Override
	public PageResponse<ExpertDTO> getMyEstimate(int pageNo, CustomUserDetails customUserDetails) {

		Long userNo = customUserDetails.getUserNo();

		int listCount = mapper.getMyEstimateCount(userNo);

		AssertUtil.notFound(listCount, "보낸 견적 요청이 없습니다.");

		Map<String, Object> params = pagenation.pageRequest(pageNo, 4, listCount);

		params.put("userNo", userNo);

		// log.info(" params 데이터 : {}" , params);

		List<ExpertDTO> list = mapper.getMyEstimate(params);
		PageInfo pageInfo = (PageInfo) params.get("pi");

		return new PageResponse<ExpertDTO>(list, pageInfo);

	}

	@Override
	public PageResponse<ResponseEstimateDTO> getReceivedEstimates(int pageNo, CustomUserDetails customUserDetails) {

		Long userNo = customUserDetails.getUserNo();

		int listCount = mapper.getResponseEstimateCount(userNo);

		AssertUtil.notFound(listCount, "받은 견적 내역이 없습니다.");

		Map<String, Object> params = pagenation.pageRequest(pageNo, 4, listCount);

		params.put("userNo", userNo);

		List<ResponseEstimateDTO> list = mapper.getResponseEstimateDetails(params);
		PageInfo pageInfo = (PageInfo) params.get("pi");

		return new PageResponse<ResponseEstimateDTO>(list, pageInfo);

	}

}
