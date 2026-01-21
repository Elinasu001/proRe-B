package com.kh.even.back.report.model.service;

import org.springframework.stereotype.Service;

import com.kh.even.back.report.model.dao.ReportMapper;
import com.kh.even.back.report.model.dto.ReportSearchDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ReportMapper reportMapper;

    @Override
    public boolean getReportStatus(Long roomNo, Long userNo) {
        ReportSearchDTO dto = new ReportSearchDTO();
        dto.setRoomNo(roomNo);
        dto.setUserNo(userNo);
        int count = reportMapper.countByRoom(dto);
        return count == 0;
    }

}
