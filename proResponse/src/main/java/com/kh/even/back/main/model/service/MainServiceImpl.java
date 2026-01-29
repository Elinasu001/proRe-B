package com.kh.even.back.main.model.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kh.even.back.main.model.dto.MainExpertDTO;
import com.kh.even.back.main.model.mapper.MainMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MainServiceImpl implements MainService {

	private final MainMapper mapper;

	@Override
	public List<MainExpertDTO> getExpertEntities() {

		return mapper.getExpertEntities();
	}

}
