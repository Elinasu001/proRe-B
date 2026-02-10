package com.kh.even.back.expert.model.dto;

import java.util.List;

import com.kh.even.back.file.model.dto.AttachmentDTO;
import com.kh.even.back.token.dto.TokensDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterResponseDTO {
	
	private TokensDTO tokens;
	private String userRole;
    private NewExpertDTO expert;       // 새로 등록한 전문가 정보       
    private List<Long> categories;     // 소분류 카테고리
    private List<AttachmentDTO> attachments;  // 상세 이미지
    
}
