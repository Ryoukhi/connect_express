package com.eadl.connect_backend.infrastructure.adapter.out.persistence.repository.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eadl.connect_backend.domain.model.user.Role;
import com.eadl.connect_backend.infrastructure.adapter.out.persistence.entity.UserEntity;

public interface AdminJpaRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByIdUser(Long idUser);

    List<UserEntity> findAllByRole(Role role);

    long countByRole(Role role);


}
