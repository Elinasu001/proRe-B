package com.kh.even.back.expert.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "TB_EXPERT")
@Getter
@NoArgsConstructor
public class ExpertEntity {

	@Id
	@Column(name = "USER_NO")
	private Long userNo;
	
	@Column(name = "CAREER")
	private int career;
	
	@Column(name ="START_TIME")
	private String startTime;
	
	@Column(name = "CONTENT")
	private String endTime;
	
	@Column(name = "EXPERT_TYPE_NO")
	private int expertTypeNo;
	
	@Column(name = "STATUS")
	private String status;
	

}
