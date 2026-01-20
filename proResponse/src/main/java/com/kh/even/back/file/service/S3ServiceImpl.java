package com.kh.even.back.file.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

@Service
@RequiredArgsConstructor
public class S3ServiceImpl implements S3Service {

	private final S3Client s3Client;
	@Value("${cloud.aws.s3.bucket}")
	private String bucketName;

	@Value("${cloud.aws.region.static}")
	private String region;

	// 업로드 메소드
	@Override
	public String store(MultipartFile file, String folderName) {

		if (file == null || file.isEmpty()) {
			return null;
		}

		String fileName = changeName(file.getOriginalFilename());

		String key = folderName + "/" + fileName;

		// s3에 업로드
		PutObjectRequest request = PutObjectRequest.builder().bucket(bucketName).key(key)
				.contentType(file.getContentType()).build();

		try {
			s3Client.putObject(request, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
		} catch (S3Exception e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("S3 업로드 실패 S3", e);
		} catch (AwsServiceException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("S3 업로드 실패 AWS", e);
		} catch (SdkClientException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("S3 업로드 실패 Sdk", e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("S3 업로드 실패 IOE", e);
		}

		String filePath = "https://" + bucketName + ".s3." + region + ".amazonaws.com/" + key;

		return filePath;
	}

	@Override
	public void deleteFile(String filePath) {
		// https://butcket-name.s3.region.amazonaws.com/넘겨받은filePath
		if (filePath == null || filePath.isEmpty())
			return;
		String key = filePath.substring(filePath.indexOf(".com/") + 5);

		try {
			DeleteObjectRequest request = DeleteObjectRequest.builder().bucket(bucketName).key(key).build();

			s3Client.deleteObject(request);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("S3파일 삭제 실패 " + e.getMessage());
		}
	}

	private String getObjectKeyFromUrl(String filePath) {

		if (filePath == null || filePath.isEmpty()) {
			return null;
		}

		try {
			URL url = new URL(filePath);
			String path = url.getPath();
			System.out.println(path);

			return path.substring(1);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	private String changeName(String origin) {

		if (origin == null || !origin.contains(".")) {
			throw new IllegalArgumentException("잘못된 파일명");
		}

		String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		int rand = (int) (Math.random() * 900) + 100;

		String ext = origin.substring(origin.lastIndexOf("."));

		return "PR_" + time + "_" + rand + ext;
	}

}
