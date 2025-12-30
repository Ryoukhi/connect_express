package com.eadl.connect_backend.infrastructure.adapter.out.persistence.repository.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eadl.connect_backend.infrastructure.adapter.out.persistence.entity.CategoryEntity;

public interface CategoryJpaRepository extends JpaRepository<CategoryEntity, Long> {

    Optional<CategoryEntity> findByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCase(String name);

    List<CategoryEntity> findByActive(boolean active);

    List<CategoryEntity> findAllByOrderByDisplayOrderAsc();

    long countByActive(boolean active);
}
