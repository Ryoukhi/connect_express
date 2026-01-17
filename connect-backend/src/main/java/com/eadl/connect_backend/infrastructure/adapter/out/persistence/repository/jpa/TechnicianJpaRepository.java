package com.eadl.connect_backend.infrastructure.adapter.out.persistence.repository.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.eadl.connect_backend.domain.model.user.Role;
import com.eadl.connect_backend.infrastructure.adapter.out.persistence.entity.UserEntity;

public interface TechnicianJpaRepository extends JpaRepository<UserEntity, Long> {

    Long countByRole(Role role);

    Optional<UserEntity> findByIdUserAndRole(Long idUser, Role role);

    List<UserEntity> findByRole(Role role);

    List<UserEntity> findByRoleAndActiveTrue(Role role);

    @Query("SELECT DISTINCT u.city FROM UserEntity u WHERE u.role = 'TECHNICIAN' AND u.active = true AND u.city IS NOT NULL ORDER BY u.city")
    List<String> findDistinctCities();

    @Query("SELECT DISTINCT u.neighborhood FROM UserEntity u WHERE u.role = 'TECHNICIAN' AND u.city = :city AND u.active = true AND u.neighborhood IS NOT NULL ORDER BY u.neighborhood")
    List<String> findDistinctNeighborhoodsByCity(@Param("city") String city);
}
