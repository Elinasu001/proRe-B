package com.kh.even.back.main.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.even.back.common.ResponseData;
import com.kh.even.back.main.model.dto.MainExpertDTO;
import com.kh.even.back.main.model.service.MainService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/main")
@RequiredArgsConstructor
public class MainController {

	private final MainService service;
	
	
	@GetMapping
	public ResponseEntity<ResponseData<List<MainExpertDTO>>> getExpertEntities(){
		
		return ResponseData.ok(service.getExpertEntities(), "조회에 성공했습니다.");
		
		
	}
	
	
}
