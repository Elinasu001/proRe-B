package com.kh.even.back.cash.model.service;

import org.springframework.stereotype.Service;

import com.kh.even.back.auth.model.vo.CustomUserDetails;
import com.kh.even.back.cash.model.mapper.CashMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CashServiceImpl implements CashService {

	private final CashMapper cashMapper;
	
	@Override
	public long getMyCash(CustomUserDetails user) {
			
		long balance = cashMapper.getMyCash(user.getUserNo());
		if(balance < 0) {
			throw new RuntimeException("캐시 조회 중 오류가 발생했습니다. 관리자에게 문의해주세요.");
		}
		
		return balance;
	}
}
