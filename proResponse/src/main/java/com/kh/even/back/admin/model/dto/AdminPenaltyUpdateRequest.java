package com.kh.even.back.admin.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 관리자 회원 징계 상태 변경 요청 DTO
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AdminPenaltyUpdateRequest {
    
    @Pattern(regexp = "^[YN]$", message = "징계 상태값은 Y 또는 N만 가능합니다.")
    private char penaltyStatus;  // 'Y': 징계, 'N': 정상
}