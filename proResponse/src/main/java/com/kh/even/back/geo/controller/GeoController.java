package com.kh.even.back.geo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kh.even.back.common.ResponseData;
import com.kh.even.back.geo.model.dto.GeoCodeResponseDTO;
import com.kh.even.back.geo.model.service.GeoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/geo")
public class GeoController {

    private final GeoService geoService;

    @GetMapping("/geocode")
    public ResponseEntity<ResponseData<GeoCodeResponseDTO>> geocode(@RequestParam("query") String query) {
    	
        GeoCodeResponseDTO data = geoService.geocode(query);
        
        return ResponseData.ok(data, "좌표 변환에 성공했습니다.");
    }
    
}
