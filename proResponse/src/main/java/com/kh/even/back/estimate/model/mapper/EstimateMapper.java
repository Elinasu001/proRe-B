package com.kh.even.back.estimate.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.kh.even.back.expert.model.dto.ExpertDTO;
import com.kh.even.back.file.model.vo.FileVO;

@Mapper
public interface EstimateMapper {

	void saveEstimateAttachment(FileVO file);

	List<ExpertDTO> getMyEstimate(Long userNo);
	
}
