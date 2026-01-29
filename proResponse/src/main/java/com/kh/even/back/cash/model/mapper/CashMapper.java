package com.kh.even.back.cash.model.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CashMapper {
	
	/**
	 * 보유한 캐시 조회
	 * @param userNo
	 * @return 보유한 캐시
	 */
	long getMyCash(Long userNo);
}
