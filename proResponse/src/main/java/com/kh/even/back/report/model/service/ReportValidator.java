package com.kh.even.back.report.model.service;

import org.springframework.stereotype.Component;

import com.kh.even.back.exception.ReportException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ReportValidator {
    /**
     * DB 작업 결과 유효성 검사 (insert, update 등)
     */
    public static void validateDbResult(int result, String errorMessage) {
        if (result != 1) {
            throw new ReportException(errorMessage);
        }
    }
}
