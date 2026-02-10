package com.kh.even.back.expert.model.dto;

import com.kh.even.back.token.dto.TokensDTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
@Builder
public class SwitchRoleResponseDTO {
	
  private TokensDTO tokens;
  private String userRole;
  
}
