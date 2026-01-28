package com.kh.even.back.expert.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewExpertDTO {

	private Long userNo;
    private Long expertTypeNo;
    private Long career;       
    private String startTime;  
    private String endTime;    
    private String content;
    
}
