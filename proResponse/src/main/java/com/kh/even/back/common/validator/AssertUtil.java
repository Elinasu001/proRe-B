package com.kh.even.back.common.validator;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.tika.Tika;
import org.springframework.web.multipart.MultipartFile;

import com.kh.even.back.exception.InvalidFileException;
import com.kh.even.back.exception.NotFoundException;

public class AssertUtil {

	private AssertUtil() {
	}

	public static void notFound(int count, String message) {

		if (count <= 0) {
			throw new NotFoundException(message);
		}

	}

	
	public static void validateImageFiles(List<MultipartFile> files) {

		checkFileSize(files);
		System.out.println("1");
		if (files == null || files.isEmpty()) {
			return;
		}
		System.out.println("2");
		
		for (MultipartFile file : files) {
			System.out.println("3");
			validateImageFile(file);
		}
	}

	private static void checkFileSize(List<MultipartFile> files) {
		if (files != null && files.size() > 4) {
			throw new InvalidFileException("첨부파일은 최대 4개까지 업로드할 수 있습니다.");
		}
	}
	
	private static void validateImageFile(MultipartFile file) {

		Tika tika = new Tika();

		try (InputStream inputStream = file.getInputStream()) {

			String mimeType = tika.detect(inputStream);

			if (!isAllowedImageMime(mimeType)) {
				throw new InvalidFileException("허용되지 않은 이미지 형식입니다.");
			}

		} catch (IOException e) {
			throw new InvalidFileException("파일 검증 중 오류가 발생했습니다.");
		}
	}

	private static boolean isAllowedImageMime(String mimeType) {
		return "image/jpeg".equalsIgnoreCase(mimeType)
			|| "image/png".equalsIgnoreCase(mimeType)
			|| "image/gif".equalsIgnoreCase(mimeType)
			|| "image/bmp".equalsIgnoreCase(mimeType);
	}

}
