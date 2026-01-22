package com.kh.even.back.expert.model.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.kh.even.back.expert.model.dto.ExpertDetailDTO;

@Mapper
public interface ExpertMapper {

	ExpertDetailDTO getExpertDetails(Map<String,Long> param);
	
	int getCountByExpertNo(Long expertNo);
}
