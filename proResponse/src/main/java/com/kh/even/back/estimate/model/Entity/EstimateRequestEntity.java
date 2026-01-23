package com.kh.even.back.estimate.model.Entity;

import java.sql.Date;

import com.kh.even.back.estimate.model.status.EstimateRequestStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "TB_ESTIMATE_REQUEST")
@SequenceGenerator(name = "estimate_request_seq", sequenceName = "SEQ_ESTIMATE_REQUEST", allocationSize = 1)
public class EstimateRequestEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "estimate_request_seq")
	@Column(name = "REQUEST_NO", nullable = false)
	private Long requestNo;

	@Column(name = "REQUEST_DATE", nullable = false)
	private Date requestDate;

	@Column(name = "REQUEST_TYPE", nullable = false)
	private String requestType;

	@Column(name = "REQUEST_SERVICE", nullable = false)
	private String requestService;

	@Column(name = "CONTENT", nullable = false)
	private String content;

	@Column(name = "USER_NO", nullable = false)
	private Long userNo;

	@Column(name = "EXPERT_NO", nullable = false)
	private Long expertNo;

	@Column(name = "CATEGORY_DETAIL_NO", nullable = false)
	private Long categoryDetailNo;

	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS", nullable = false)
	private EstimateRequestStatus status;

	public void changeStatus(EstimateRequestStatus status) {
	    this.status = status;
	}
	
}
