package com.kh.even.back.expert.model.entity;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "TB_ESTIMATE_RESPONSE")
@SequenceGenerator(name = "expert_estimate_seq", sequenceName = "SEQ_ESTIMATE_RESPONSE", allocationSize = 1)
public class ExpertEstimateEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "expert_estimate_seq")
	@Column(name = "ESTIMATE_NO" , nullable = false)
	private Long estimateNo;
	
	@Column(name = "PRICE" , nullable = false)
	private int price;
	
	@Column(name = "CONTENT" , nullable = false)
	private String content;
	
	@Column(name = "ESTIMATE_DATE" , nullable = false , insertable = false, updatable = false)
	private Date estimateDate;
	
	@Column(name = "STATUS" , nullable = false , insertable = false)
	private String status;
	
	@Column(name = "DELETE_AT" , nullable = false , insertable = false)
	private String deleteAt;
	
	@Column(name = "REQUEST_NO" , nullable = false)
	private Long requestNo;
	
	
}
