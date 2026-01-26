package com.kh.even.back.expert.model.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.kh.even.back.category.model.dto.DetailCategoryDTO;
import com.kh.even.back.category.model.dto.ExpertListDTO;
import com.kh.even.back.estimate.model.dto.ExpertRequestUserDTO;
import com.kh.even.back.expert.model.dto.ExpertDetailDTO;
import com.kh.even.back.expert.model.dto.ExpertLocationDTO;
import com.kh.even.back.file.model.vo.FileVO;

@Mapper
public interface ExpertMapper {

	ExpertDetailDTO getExpertDetails(Map<String, Long> param);

	int getCountByExpertNo(Long expertNo);
	
	int countByRequestNoAndUserNo(@Param(value = "requestNo") Long requestNo, @Param(value = "userNo") Long userNo);

	void saveExpertEstimateAttachment(FileVO file);

	int countMatchedByUserNo(Long userNo);

	List<ExpertRequestUserDTO> getMatchedUser(Map<String, Object> params);

	List<DetailCategoryDTO> getExpertCategories(Long expertNo);
	
	List<ExpertLocationDTO> getExpertLocations();
	
	int getLikedExpertsCount(Long userNo);
	
	List<ExpertListDTO> getLikedExperts(Map<String, Object> params);
	
	
}
