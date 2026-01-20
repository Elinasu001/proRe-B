package com.kh.even.back.estimate.model.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.kh.even.back.auth.model.vo.CustomUserDetails;
import com.kh.even.back.estimate.model.Entity.EstimateRequestEntity;
import com.kh.even.back.estimate.model.dto.EstimateRequestDTO;
import com.kh.even.back.estimate.model.mapper.EstimateMapper;
import com.kh.even.back.estimate.model.repository.EstimateRepository;
import com.kh.even.back.file.model.vo.FileVO;
import com.kh.even.back.file.service.S3Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EstimateSerivceImpl implements EstimateService {

	private final S3Service s3Service;
	private final EstimateMapper mapper;
	private final EstimateRepository repository;

	@Override
	@Transactional
	public void saveEstimate(EstimateRequestDTO estimateReqeust, List<MultipartFile> files , CustomUserDetails customUserDetails) {

		EstimateRequestEntity entity = toEntity(estimateReqeust , customUserDetails);

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

	private EstimateRequestEntity toEntity(EstimateRequestDTO dto,CustomUserDetails customUserDetails) {

		EstimateRequestEntity entity = EstimateRequestEntity.builder()
				        .requestDate(dto.getRequestDate())
						.requestType(dto.getRequestType())
						.requestService(dto.getRequestService())
						.content(dto.getContent())
						.expertNo(dto.getExpertNo())
						.categoryDetailNo(dto.getCategoryDetailNo())
						.userNo(customUserDetails.getUserNo())
						.build();


		return entity;
	}

}
