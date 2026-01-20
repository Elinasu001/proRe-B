package com.kh.even.back.estimate.model.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.kh.even.back.expert.model.dto.ExpertDTO;
import com.kh.even.back.expert.model.dto.ResponseEstimateDTO;
import com.kh.even.back.file.model.vo.FileVO;

@Mapper
public interface EstimateMapper {

	void saveEstimateAttachment(FileVO file);

	List<ExpertDTO> getMyEstimate(Map<String, Object> params);

	int getMyEstimateCount(Long userNo);
	
	int getResponseEstimateCount(Long userNo);
	
	List<ResponseEstimateDTO> getResponseEstimateDetails(Map<String, Object> params);
}
