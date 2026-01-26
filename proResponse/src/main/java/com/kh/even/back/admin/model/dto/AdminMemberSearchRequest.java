package com.kh.even.back.admin.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminMemberSearchRequest {
    
    @Min(value = 1, message = "페이지는 1 이상이어야 합니다")
    private int currentPage = 1;
    
    @Size(max = 50, message = "검색어는 50자 이하여야 합니다")
    private String keyword;
}