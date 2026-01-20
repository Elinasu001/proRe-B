package com.kh.even.back.category.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "TB_EXPERT_TYPE")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CategoryEntity {

	@Id
	@Column(name = "EXPERT_TYPE_NO")
	private Long categoryNo;

	@Column(name = "EXPERT_NAME")
	private String expertName;
}