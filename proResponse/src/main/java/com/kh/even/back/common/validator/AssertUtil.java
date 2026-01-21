package com.kh.even.back.common.validator;

import java.util.List;

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

	public static void checkFileSize(List<MultipartFile> files) {

		if (files != null && files.size() > 4) {
			throw new InvalidFileException("첨부파일은 최대 4개까지 업로드할 수 있습니다.");
		}

	}

}
