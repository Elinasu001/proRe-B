package com.kh.even.back.expert.model.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.kh.even.back.auth.model.vo.CustomUserDetails;
import com.kh.even.back.common.validator.AssertUtil;
import com.kh.even.back.expert.model.dto.ExpertDetailDTO;
import com.kh.even.back.expert.model.mapper.ExpertMapper;
import com.kh.even.back.expert.model.repository.ExpertRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExpertServiceImpl implements ExpertService {

	private final ExpertMapper mapper;
	private final ExpertRepository repository;

	public ExpertDetailDTO getExpertDetails(Long expertNo, CustomUserDetails user) {

		Long userNo = null;

		if (user != null) {
			userNo = user.getUserNo();
		}
		
		Map<String, Long> param = new HashMap();
		
		param.put("expertNo", expertNo);
		param.put("userNo", userNo );
		validateExpertCount(expertNo);

		ExpertDetailDTO expertDetail = mapper.getExpertDetails(param);

		// log.info("expertDetail 체크 : {} " , expertDetail);

		return expertDetail;
	}

	private void validateExpertCount(Long expertNo) {
		
		int count = repository.countByUserNo(expertNo);

		// log.info(" JPA로 count 잘 들어오는지 확인 : {} " , count);

		AssertUtil.notFound(count, "유효하지 않은 전문가 번호입니다.");

	}

}
