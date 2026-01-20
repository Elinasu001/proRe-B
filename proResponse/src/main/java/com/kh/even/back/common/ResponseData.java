package com.kh.even.back.common;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.kh.even.back.review.model.vo.ReviewVO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseData<T> {
	private String message;
	private T data;
	private boolean success;
	private LocalDateTime timestamp;

	private ResponseData(String message, T data, boolean success, LocalDateTime timestamp) {
		this.message = message;
		this.data = data;
		this.success = success;
		this.timestamp = timestamp;

	}

	// 성공 응답
	public static <T> ResponseEntity<ResponseData<T>> ok(T data) {
		return ResponseEntity.ok(new ResponseData<T>(null, data, true, LocalDateTime.now()));

	}

	public static <T> ResponseEntity<ResponseData<T>> ok(T data, String message) {
		return ResponseEntity.ok(new ResponseData<T>(message, data, true, LocalDateTime.now()));
	}

	// 실패응답
	public static <T> ResponseEntity<ResponseData<T>> failure(String message, HttpStatus status) {
		return ResponseEntity.status(status).body(new ResponseData<T>(message, null, false, LocalDateTime.now()));
	}

	// 3. 데이터 없이 상태 코드만 보내는 경우 (204 No Content)
	public static <T> ResponseEntity<ResponseData<T>> noContent() {
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	// 4. 생성 성공 (201 Created)
	public static <T> ResponseEntity<ResponseData<T>> created(T data, String message) {
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new ResponseData<>(message, data, true, LocalDateTime.now()));
	}

}