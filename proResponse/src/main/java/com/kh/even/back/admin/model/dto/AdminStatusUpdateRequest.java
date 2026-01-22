package com.kh.even.back.admin.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 관리자 회원 상태 변경 요청 DTO
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AdminStatusUpdateRequest {
    
    @NotNull(message = "상태값은 필수입니다.")
    @Pattern(regexp = "^[YN]$", message = "상태값은 Y 또는 N만 가능합니다.")
    private char status;  // 'Y': 활성, 'N': 탈퇴
}