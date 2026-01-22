package com.kh.even.back.expert.model.service;

import com.kh.even.back.auth.model.vo.CustomUserDetails;
import com.kh.even.back.expert.model.dto.ExpertDetailDTO;

public interface ExpertService {

	/**
	 * 
	 * @param expertNo 앞단에서 가져오는 전문가 번호
	 * @param user 로그인 유저
	 * @return 전문가 자세히보기를 눌렀을때 나오는 정보를 담은 DTO
	 */
	ExpertDetailDTO getExpertDetails(Long expertNo, CustomUserDetails user);

}
