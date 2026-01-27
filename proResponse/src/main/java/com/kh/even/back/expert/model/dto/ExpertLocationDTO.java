package com.kh.even.back.expert.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ExpertLocationDTO {

	
	private Long expertNo;
    private Double latitude;
    private Double longitude;
    
}
