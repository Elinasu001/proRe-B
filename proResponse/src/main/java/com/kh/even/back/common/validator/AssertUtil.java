package com.kh.even.back.common.validator;

import com.kh.even.back.exception.NotFoundException;

public class AssertUtil {

	private AssertUtil() {
	}

	public static void notFound(int count, String message) {

		if (count <= 0) {

			throw new NotFoundException(message);

		}

	}
	

}
