package com.kh.even.back.cash.model.service;

import com.kh.even.back.auth.model.vo.CustomUserDetails;

public interface CashService {

	long getMyCash(CustomUserDetails user);
}
