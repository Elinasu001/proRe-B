package com.kh.even.back.category.model.dto;

import java.sql.Date;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class ExpertVO {

	private Long userNo;
	private String userName;
	private double starScore;
	private int likeCount;
	private String address;
	private int career;
	private Date startTime;
	private Date endTime;
	private boolean isLiked;
	private String profileImg;

}
