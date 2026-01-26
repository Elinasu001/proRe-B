package com.kh.even.back.estimate.model.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.kh.even.back.estimate.model.dto.EstimateRequestDetailDTO;
import com.kh.even.back.estimate.model.dto.EstimateResponseDetailDTO;
import com.kh.even.back.estimate.model.dto.ExpertRequestUserDTO;
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

	int getReceivedRequestsCount(Long expertNo);

	List<ExpertRequestUserDTO> getReceivedRequests(Map<String, Object> params);

	int getCheckEstimateCountRequested(@Param("userNo") Long userNo, @Param("requestNo") Long requestNo);

	EstimateRequestDetailDTO getEstimateDetail(Long requestNo);

	int getCheckEstimateCountOuoted(@Param("userNo") Long userNo, @Param("requestNo") Long requestNo);

	int getCheckEstimateCount(@Param("userNo") Long userNo, @Param("requestNo") Long requestNo);

	EstimateResponseDetailDTO getReceivedEstimateDetail(Long requestNo);

	void updateEstimateContent(@Param("requestNo") Long requestNo, @Param("content") String content);
	
	void softDeleteAllAttachments(Long requestNo);
	
	List<String> findAllbyRequestNo(Long requestNo);
}
