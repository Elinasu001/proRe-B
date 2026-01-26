package com.kh.even.back.expert.model.dto;

import java.util.List;

import com.kh.even.back.expert.model.dto.LargeCategoryDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponseDTO {
	
	private List<LargeCategoryDTO> expertTypes;
}
