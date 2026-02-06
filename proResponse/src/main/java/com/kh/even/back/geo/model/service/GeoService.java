package com.kh.even.back.geo.model.service;

import com.kh.even.back.geo.model.dto.GeoCodeResponseDTO;

public interface GeoService {

	GeoCodeResponseDTO geocode(String query);
	
}
