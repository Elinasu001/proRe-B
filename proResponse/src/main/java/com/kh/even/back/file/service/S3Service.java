package com.kh.even.back.file.service;

import org.springframework.web.multipart.MultipartFile;

public interface S3Service {
	String store(MultipartFile file);

	void deleteFile(String filePath);
	

}
