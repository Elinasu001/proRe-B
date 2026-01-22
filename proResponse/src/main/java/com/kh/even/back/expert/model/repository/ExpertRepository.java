package com.kh.even.back.expert.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kh.even.back.expert.model.entity.ExpertEntity;

public interface ExpertRepository extends JpaRepository<ExpertEntity, Long> {

	/**
	 * 
	 * @param userNo
	 * @return userNo 로 전문가가 있는지 확인해서 int를 돌려줌
	 */
	int countByUserNo(Long userNo);

}
