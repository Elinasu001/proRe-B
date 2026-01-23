package com.kh.even.back.estimate.model.dto;

import java.sql.Date;
import java.util.List;

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
public class EstimateRequestDetailDTO {

	private Long requestNo;
	private String profileImg;
	private String nickname;
	private String categoryName;
	private Date requestDate;
	private String address;
	private String requestType;
	private String requestService;
	private String content;
	private List<String> filePaths;
	

}
