package com.kh.even.back.estimate.model.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.kh.even.back.auth.model.vo.CustomUserDetails;
import com.kh.even.back.common.validator.AssertUtil;
import com.kh.even.back.estimate.model.Entity.EstimateRequestEntity;
import com.kh.even.back.estimate.model.dto.EstimateRequestDTO;
import com.kh.even.back.estimate.model.dto.EstimateRequestDetailDTO;
import com.kh.even.back.estimate.model.dto.EstimateRequestUpdateDTO;
import com.kh.even.back.estimate.model.dto.EstimateResponseDetailDTO;
import com.kh.even.back.estimate.model.dto.ExpertRequestUserDTO;
import com.kh.even.back.estimate.model.mapper.EstimateMapper;
import com.kh.even.back.estimate.model.repository.EstimateRepository;
import com.kh.even.back.estimate.model.status.EstimateRequestStatus;
import com.kh.even.back.exception.EntityNotFoundException;
import com.kh.even.back.expert.model.dto.ExpertDTO;
import com.kh.even.back.expert.model.dto.ResponseEstimateDTO;
import com.kh.even.back.expert.model.entity.ExpertEstimateEntity;
import com.kh.even.back.expert.model.mapper.ExpertMapper;
import com.kh.even.back.expert.model.repository.ExpertEstimateRepository;
import com.kh.even.back.expert.model.status.EstimateResponseStatus;
import com.kh.even.back.file.service.FileUploadService;
import com.kh.even.back.file.service.S3Service;
import com.kh.even.back.util.Pagenation;
import com.kh.even.back.util.PagingExecutor;
import com.kh.even.back.util.model.dto.PageResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EstimateSerivceImpl implements EstimateService {

	private final FileUploadService fileUploadService;
	private final EstimateMapper mapper;
	private final ExpertMapper expertMapper;
	private final EstimateRepository repository;
	private final ExpertEstimateRepository expertRepository;
	private final Pagenation pagenation;
	private final S3Service s3Service;
	private final PagingExecutor pagingExecutor;
	
	@Override
	@Transactional
	public void saveEstimate(EstimateRequestDTO estimateReqeust, List<MultipartFile> files,
			CustomUserDetails customUserDetails) {

		AssertUtil.validateImageFiles(files);

		EstimateRequestEntity entity = toEntity(estimateReqeust, customUserDetails);

		EstimateRequestEntity savedEntity = repository.save(entity);

		fileUploadService.uploadFiles(files, "estimate", savedEntity.getRequestNo(), mapper::saveEstimateAttachment);

	}

	// EstimateRequesdto -> entity
	private EstimateRequestEntity toEntity(EstimateRequestDTO dto, CustomUserDetails customUserDetails) {

		EstimateRequestEntity entity = EstimateRequestEntity.builder().requestDate(dto.getRequestDate())
				.requestType(dto.getRequestType()).requestService(dto.getRequestService()).content(dto.getContent())
				.expertNo(dto.getExpertNo()).categoryDetailNo(dto.getCategoryDetailNo())
				.userNo(customUserDetails.getUserNo()).status(EstimateRequestStatus.REQUESTED).build();

		return entity;
	}

	@Override
	public PageResponse<ExpertDTO> getMyEstimate(int pageNo, CustomUserDetails customUserDetails) {

		Long userNo = customUserDetails.getUserNo();

		return pagingExecutor.execute(
				pageNo,
				4,
				mapper.getMyEstimateCount(userNo),
				"보낸 견적 요청이 없습니다.",
				params -> params.put("userNo", userNo),
				mapper::getMyEstimate
		);
	}

	@Override
	public PageResponse<ResponseEstimateDTO> getReceivedEstimates(int pageNo, CustomUserDetails customUserDetails) {

		Long userNo = customUserDetails.getUserNo();

		return pagingExecutor.execute(
				pageNo,
				4,
				mapper.getResponseEstimateCount(userNo),
				"받은 견적 내역이 없습니다.",
				params -> params.put("userNo", userNo),
				mapper::getResponseEstimateDetails
		);
	}

	@Override
	public PageResponse<ExpertRequestUserDTO> getReceivedRequests(int pageNo, CustomUserDetails customUserDetails) {

		Long expertNo = customUserDetails.getUserNo();

		return pagingExecutor.execute(
				pageNo,
				4,
				mapper.getReceivedRequestsCount(expertNo),
				"받은 견적 요청이 없습니다.",
				params -> params.put("expertNo", expertNo),
				mapper::getReceivedRequests
		);
	}

	@Override
	public EstimateRequestDetailDTO getEstimateDetail(CustomUserDetails customUserDetails, Long requestNo) {

		Long userNo = customUserDetails.getUserNo();

		int count = mapper.getCheckEstimateCountRequested(userNo, requestNo);

		AssertUtil.notFound(count, "해당하는 견적을 찾을 수 없거나 회원의 견적이 아닙니다.");

		return mapper.getEstimateDetail(requestNo);

	}

	@Override
	@Transactional
	public void updateEstimateStatus(Long requestNo, CustomUserDetails customUserDetails) {

		Long userNo = customUserDetails.getUserNo();

		int count = mapper.getCheckEstimateCountOuoted(userNo, requestNo);

		AssertUtil.notFound(count, "해당하는 견적을 찾을 수 없거나 회원의 견적이 아닙니다.");

		EstimateRequestEntity requestEntity = repository.findById(requestNo)
				.orElseThrow(() -> new EntityNotFoundException("유효하지 않은 요청 번호입니다."));

		requestEntity.changeStatus(EstimateRequestStatus.MATCHED);

		ExpertEstimateEntity responseEntity = expertRepository.findByRequestNo(requestNo);

		responseEntity.changeStatus(EstimateResponseStatus.ACCEPTED);

	}

	@Override
	public EstimateResponseDetailDTO getReceivedEstimateDetail(CustomUserDetails customUserDetails, Long requestNo) {

		Long userNo = customUserDetails.getUserNo();

		int count = mapper.getCheckEstimateCount(userNo, requestNo);

		AssertUtil.notFound(count, "해당하는 견적을 찾을 수 없거나 회원의 견적이 아닙니다.");

		return mapper.getReceivedEstimateDetail(requestNo);

	}

	@Override
	@Transactional
	public void updateRequestEstimate(Long requestNo, EstimateRequestUpdateDTO dto, List<MultipartFile> images,
			CustomUserDetails user) {

		Long userNo = user.getUserNo();

		AssertUtil.notFound(mapper.getCheckEstimateCountRequested(userNo, requestNo),
				"해당하는 견적을 찾을 수 없거나 회원의 견적이 아닙니다.");

		AssertUtil.validateImageFiles(images);

		List<String> filePaths = mapper.findAllbyRequestNo(requestNo);

		// 기존 컨텐츠 수정
		mapper.updateEstimateContent(requestNo, dto.getContent());

		// 기존 컨텐츠 이미지 다 Y로 softDelete
		mapper.softDeleteAllAttachments(requestNo);

		// 기존 filePath s3에서 삭제
		filePaths.forEach(s3Service::deleteFile);

		// 5. 새 이미지 업로드 + DB 저장
		fileUploadService.uploadFiles(images, "estimate", requestNo, mapper::saveEstimateAttachment);

	}

	@Override
	@Transactional
	public void deleteEstimateByRequestNo(Long requestNo, CustomUserDetails user) {

		Long userNo = user.getUserNo();

		AssertUtil.notFound(expertMapper.countByRequestNoAndUserNo(requestNo, userNo), "회원의 견적이 아니거나 견적을 찾을수가 없습니다.");

		// 1. 삭제 대상 파일 목록 먼저 확보
		List<String> requestFiles = mapper.findAllbyRequestNo(requestNo);
		List<String> responseFiles = expertMapper.findAllbyRequestNo(requestNo);

		// 2. DB soft delete 먼저 (트랜잭션 보장)
		mapper.softDeleteAllAttachments(requestNo);
		mapper.updateStatusByRequestNo(requestNo);

		expertMapper.softDeleteAllAttachments(requestNo);
		expertMapper.updateStatusByRequestNo(requestNo);

		// 3. 트랜잭션 성공 후 S3 삭제
		requestFiles.forEach(s3Service::deleteFile);
		responseFiles.forEach(s3Service::deleteFile);

	}

}
