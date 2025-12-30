package com.eadl.connect_backend.infrastructure.adapter.out.persistence.repository.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eadl.connect_backend.infrastructure.adapter.out.persistence.entity.UserEntity;

public interface ClientJpaRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUser_IdUser(Long idUser);

    List<UserEntity> findByAddressContainingIgnoreCase(String address);

    List<UserEntity> findByActive(boolean active);

    List<UserEntity> findByCityIgnoreCase(String city);

    long countByActive(boolean active);
}
