package com.kh.even.back.geo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeoCodeResponseDTO {

	private String lat; // y (위도)
	private String lng; // x (경도)
	
}
