package com.kh.even.back.estimate.model.dto;

import java.time.LocalDateTime;
import jakarta.validation.constraints.NotNull;
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
public class EstimateRequestDTO {

	@NotNull
	private LocalDateTime requestDate;

	@NotNull
	private String requestType;

	@NotNull
	private String requestService;

	@NotNull
	private String content;

	@NotNull
	private Long expertNo;

	@NotNull
	private Long categoryDetailNo;

}
