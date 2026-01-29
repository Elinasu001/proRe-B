package com.kh.even.back.main.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.kh.even.back.main.model.dto.MainExpertDTO;

@Mapper
public interface MainMapper {

	
	List<MainExpertDTO> getExpertEntities();
	
}
