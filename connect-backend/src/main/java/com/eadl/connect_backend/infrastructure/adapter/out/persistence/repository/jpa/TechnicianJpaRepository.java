package com.eadl.connect_backend.infrastructure.adapter.out.persistence.repository.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eadl.connect_backend.domain.model.user.Role;
import com.eadl.connect_backend.infrastructure.adapter.out.persistence.entity.UserEntity;

public interface TechnicianJpaRepository extends JpaRepository<UserEntity, Long> {

    Long countByRole(Role role);

    Optional<UserEntity> findByIdUserAndRole(Long idUser, Role role);

    List<UserEntity> findByRole(Role role);

     List<UserEntity> findByRoleAndActiveTrue(Role role, boolean active);
}
