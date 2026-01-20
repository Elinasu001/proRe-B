package com.kh.even.back.estimate.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kh.even.back.estimate.model.Entity.EstimateRequestEntity;

public interface EstimateRepository extends JpaRepository<EstimateRequestEntity, Long> {

}
