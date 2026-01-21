package com.kh.even.back.report.model.dao;

import org.apache.ibatis.annotations.Mapper;

import com.kh.even.back.report.model.dto.ReportSearchDTO;
@Mapper
public interface ReportMapper {

    int countByRoom(ReportSearchDTO dto);

}
