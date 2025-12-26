package com.eadl.connect_backend.infrastructure.repository.jpa;

import com.eadl.connect_backend.domain.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository Spring Data JPA pour l'entité Category
 */
@Repository
public interface CategoryJpaRepository extends JpaRepository<Category, Long> {

}