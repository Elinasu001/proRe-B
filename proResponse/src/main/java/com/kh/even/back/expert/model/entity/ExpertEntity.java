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

}
