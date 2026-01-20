package com.kh.even.back.file.model.vo;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class FileVO {

	private Long fileNo;
	private String originName;
	private String filePath;
	private String status;
	private Long reqNo;
	
}
