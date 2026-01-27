package com.kh.even.back.expert.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.kh.even.back.exception.NotFoundException;
import com.kh.even.back.expert.model.dto.ExpertDetailDTO;
import com.kh.even.back.expert.model.dto.ExpertEstimateDTO;
import com.kh.even.back.expert.model.dto.ExpertLocationDTO;
import com.kh.even.back.expert.model.dto.ExpertSearchDTO;
import com.kh.even.back.expert.model.dto.LargeCategoryDTO;
import com.kh.even.back.expert.model.entity.ExpertEstimateEntity;
import com.kh.even.back.expert.model.mapper.ExpertMapper;
import com.kh.even.back.expert.model.repository.ExpertEstimateRepository;
import com.kh.even.back.expert.model.repository.ExpertRepository;
import com.kh.even.back.expert.model.status.EstimateResponseStatus;
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
		boolean isExpert = user.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_EXPERT"));
		if(isExpert) {
			throw new CustomAuthorizationException("이미 전문가인 회원입니다.");
		}
		
		List<LargeCategoryDTO> categories = mapper.getExpertCategory();
		if(categories == null || categories.isEmpty()) {
			throw new NotFoundException("카테고리 조회에 실패했습니다.");
		}
		
		return categories;
	}
}
