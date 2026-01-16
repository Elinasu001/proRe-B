package com.kh.even.back.category.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;


import com.kh.even.back.category.model.entity.CategoryEntity;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

}
