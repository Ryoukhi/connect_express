package com.eadl.connect_backend.infrastructure.adapter.out.persistence.adapter;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.eadl.connect_backend.domain.model.user.Admin;
import com.eadl.connect_backend.domain.model.user.Role;
import com.eadl.connect_backend.domain.port.out.persistence.AdminRepository;
import com.eadl.connect_backend.infrastructure.adapter.out.persistence.entity.UserEntity;
import com.eadl.connect_backend.infrastructure.adapter.out.persistence.mapper.UserEntityMapper;
import com.eadl.connect_backend.infrastructure.adapter.out.persistence.repository.jpa.AdminJpaRepository;

@Repository
public class AdminRepositoryAdapter implements AdminRepository{

    private final AdminJpaRepository adminJpaRepository;
    private final UserEntityMapper userEntityMapper;

    public AdminRepositoryAdapter(AdminJpaRepository adminJpaRepository, UserEntityMapper userEntityMapper) {
        this.adminJpaRepository = adminJpaRepository;
        this.userEntityMapper = userEntityMapper;
    }

    @Override
    public Admin save(Admin admin) {
        UserEntity userEntity = userEntityMapper.toEntity(admin);
        UserEntity savedEntity = adminJpaRepository.save(userEntity);
        return (Admin) userEntityMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Admin> findById(Long idAdmin) {
        return adminJpaRepository.findById(idAdmin)
            .filter(entity -> entity.getRole() == Role.ADMIN)
            .map(userEntityMapper::toDomain)
            .map(user -> (Admin) user);
    }

    @Override
    public Optional<Admin> findByUserId(Long idUser) {
        return adminJpaRepository.findByIdUser(idUser)
            .filter(entity -> entity.getRole() == Role.ADMIN)
            .map(userEntityMapper::toDomain)
            .map(user -> (Admin) user);
    }

    @Override
    public List<Admin> findAll() {
        List<UserEntity> entities = adminJpaRepository.findAllByRole(Role.ADMIN);
        return userEntityMapper.toDomains(entities).stream()
            .map(user -> (Admin) user)
            .toList();
    }

    @Override
    public Long count() {
        return adminJpaRepository.countByRole(Role.ADMIN);
    }

}
