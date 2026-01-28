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
import com.kh.even.back.expert.model.dto.ExpertSearchDTO;
import com.kh.even.back.expert.model.dto.LargeCategoryDTO;
import com.kh.even.back.expert.model.dto.RegisterResponseDTO;
import com.kh.even.back.expert.model.vo.ExpertRegisterVO;
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
	
	List<String> findAllbyRequestNo(Long requestNo);
	
	void softDeleteAllAttachments(Long requestNo);
	
	void updateStatusByRequestNo(Long requestNo);
	
	int countExpertsByKeyword(String keyword);
	
	List<ExpertSearchDTO> getExpertsByNickname(Map<String, Object> params);
	
	List<LargeCategoryDTO> getExpertCategory();
	
	int insertExpert(ExpertRegisterVO registerVO);
	
	int insertExpertCategoryDetail(@Param("userNo")Long userNo, @Param("categoryDetailNo")Long categoryDetailNo);
	
	void insertExpertAttachment(FileVO file);
	
	int updateRoleToExpert(Long userNo);
	
	List<RegisterResponseDTO> getNewExpert(Long userNo);
	
	int updateExpert(ExpertRegisterVO registerVO);
	
	int updateExpertCategoryDetail(@Param("userNo")Long userNo, @Param("categoryDetailNo")Long categoryDetailNo);
	
	void updateExpertAttachment(FileVO file);
	
}
