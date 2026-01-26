package com.kh.even.back.like.model.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class LikeVO {
	private Long likeNo;
	private Long expertNo;
	private Long userNo;
	private String createDate;
}
