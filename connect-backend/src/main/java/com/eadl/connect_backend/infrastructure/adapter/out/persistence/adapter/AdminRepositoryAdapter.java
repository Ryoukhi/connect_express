package com.eadl.connect_backend.infrastructure.adapter.out.persistence.adapter;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.eadl.connect_backend.domain.model.user.Admin;
import com.eadl.connect_backend.domain.model.user.Role;
import com.eadl.connect_backend.domain.port.out.persistence.AdminRepository;
import com.eadl.connect_backend.infrastructure.adapter.out.persistence.entity.UserEntity;
import com.eadl.connect_backend.infrastructure.adapter.out.persistence.mapper.UserEntityMapper;
import com.eadl.connect_backend.infrastructure.adapter.out.persistence.repository.jpa.AdminJpaRepository;

@Repository
public class AdminRepositoryAdapter implements AdminRepository {

    private static final Logger log = LoggerFactory.getLogger(AdminRepositoryAdapter.class);

    private final AdminJpaRepository adminJpaRepository;
    private final UserEntityMapper userEntityMapper;

    public AdminRepositoryAdapter(AdminJpaRepository adminJpaRepository, UserEntityMapper userEntityMapper) {
        this.adminJpaRepository = adminJpaRepository;
        this.userEntityMapper = userEntityMapper;
    }

    @Override
    public Admin save(Admin admin) {
        log.info("Saving Admin: {}", admin);

        UserEntity userEntity = userEntityMapper.toEntity(admin);
        UserEntity savedEntity = adminJpaRepository.save(userEntity);
        Admin savedAdmin = (Admin) userEntityMapper.toDomain(savedEntity);

        log.info("Saved Admin: {}", savedAdmin);
        return savedAdmin;
    }

    @Override
    public Optional<Admin> findById(Long idAdmin) {
        log.info("Finding Admin by id: {}", idAdmin);

        Optional<Admin> admin = adminJpaRepository.findById(idAdmin)
                .filter(entity -> entity.getRole() == Role.ADMIN)
                .map(userEntityMapper::toDomain)
                .map(user -> (Admin) user);

        if (admin.isPresent()) {
            log.info("Found Admin: {}", admin.get());
        } else {
            log.warn("No Admin found with id: {}", idAdmin);
        }

        return admin;
    }

    @Override
    public Optional<Admin> findByidUser(Long idUser) {
        log.info("Finding Admin by user id: {}", idUser);

        Optional<Admin> admin = adminJpaRepository.findByIdUser(idUser)
                .filter(entity -> entity.getRole() == Role.ADMIN)
                .map(userEntityMapper::toDomain)
                .map(user -> (Admin) user);

        if (admin.isPresent()) {
            log.info("Found Admin by user id {}: {}", idUser, admin.get());
        } else {
            log.warn("No Admin found with user id: {}", idUser);
        }

        return admin;
    }

    @Override
    public List<Admin> findAll() {
        log.info("Fetching all Admins");

        List<UserEntity> entities = adminJpaRepository.findAllByRole(Role.ADMIN);
        List<Admin> admins = userEntityMapper.toDomains(entities).stream()
                .map(user -> (Admin) user)
                .toList();

        log.info("Found {} Admins", admins.size());
        return admins;
    }

    @Override
    public Long count() {
        log.info("Counting all Admins");
        Long count = adminJpaRepository.countByRole(Role.ADMIN);
        log.info("Total Admins: {}", count);
        return count;
    }
}
