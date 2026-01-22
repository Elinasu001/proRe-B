package com.kh.even.back.admin.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 관리자 회원 권한 변경 요청 DTO
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AdminRoleUpdateRequest {
    
    @NotBlank(message = "권한값은 필수입니다.")
    @Pattern(regexp = "^ROLE_(USER|ADMIN|MANAGER)$", message = "권한값은 ROLE_USER, ROLE_ADMIN, ROLE_MANAGER만 가능합니다.")
    private String userRole;  // ROLE_USER, ROLE_ADMIN, ROLE_MANAGER 등
}