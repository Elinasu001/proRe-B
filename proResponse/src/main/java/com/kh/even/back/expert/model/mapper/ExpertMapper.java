package com.kh.even.back.expert.model.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.kh.even.back.expert.model.dto.ExpertDetailDTO;
import com.kh.even.back.file.model.vo.FileVO;

@Mapper
public interface ExpertMapper {

	ExpertDetailDTO getExpertDetails(Map<String,Long> param);
	
	int getCountByExpertNo(Long expertNo);
	
	//@Select("SELECT COUNT(*) FROM TB_ESTIMATE_REQUEST WHERE REQUEST_NO = #{requestNo} AND EXPERT_NO = #{userNo}")
	int countByRequestNoAndUserNo(@Param(value = "requestNo") Long requestNo ,@Param(value = "userNo") Long userNo);
	
	void saveExpertEstimateAttachment(FileVO file);
}
