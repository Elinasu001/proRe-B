package com.kh.even.back.main.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MainExpertDTO {

	private Long expertNo;
	private String nickname;
	private String address;
	private String content;
	private String profileImg;
	
	
}
