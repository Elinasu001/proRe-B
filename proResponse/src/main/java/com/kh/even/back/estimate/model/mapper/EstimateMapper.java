package com.kh.even.back.estimate.model.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.kh.even.back.file.model.vo.FileVO;

@Mapper
public interface EstimateMapper {

	void saveEstimateAttachment(FileVO file);

}
