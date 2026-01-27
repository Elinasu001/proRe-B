package com.kh.even.back.estimate.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ExpertRequestUserDTO {

	private Long requestNo;
	private Long userNo;
	private String nickname;
	private String address;
	private String profileImg;
	
}
