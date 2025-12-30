package com.eadl.connect_backend.infrastructure.adapter.out.persistence.adapter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.eadl.connect_backend.domain.model.user.Role;
import com.eadl.connect_backend.domain.model.user.User;
import com.eadl.connect_backend.domain.port.out.persistence.UserRepository;
import com.eadl.connect_backend.infrastructure.adapter.out.persistence.entity.UserEntity;
import com.eadl.connect_backend.infrastructure.adapter.out.persistence.mapper.UserEntityMapper;
import com.eadl.connect_backend.infrastructure.adapter.out.persistence.repository.jpa.UserJpaRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepository {

    private final UserJpaRepository userJpaRepository;
    private final UserEntityMapper userEntityMapper;

    @Override
    public User save(User user) {
        UserEntity entity = userEntityMapper.toEntity(user);
        UserEntity saved = userJpaRepository.save(entity);
        return userEntityMapper.toDomain(saved);
    }

    @Override
    public Optional<User> findById(Long idUser) {
        return userJpaRepository.findById(idUser).map(userEntityMapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userJpaRepository.findByEmail(email).map(userEntityMapper::toDomain);
    }

    @Override
    public Optional<User> findByPhone(String phone) {
        return userJpaRepository.findByPhone(phone).map(userEntityMapper::toDomain);
    }

    @Override
    public List<User> findAll() {
        return userJpaRepository.findAll().stream()
                .map(userEntityMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> findByRole(Role role) {
        return userJpaRepository.findByRole(role).stream()
                .map(userEntityMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> findByActive(boolean active) {
        return userJpaRepository.findByActive(active).stream()
                .map(userEntityMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> findAll(int page, int size) {
        return userJpaRepository.findAll(PageRequest.of(page, size)).stream()
                .map(userEntityMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByEmail(String email) {
        return userJpaRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByPhone(String phone) {
        return userJpaRepository.existsByPhone(phone);
    }

    @Override
    public Long count() {
        return userJpaRepository.count();
    }

    @Override
    public Long countByRole(Role role) {
        return userJpaRepository.countByRole(role);
    }

    @Override
    public void delete(User user) {
        UserEntity entity = userEntityMapper.toEntity(user);
        userJpaRepository.delete(entity);
    }

    @Override
    public void deleteById(Long idUser) {
        userJpaRepository.deleteById(idUser);
    }

    @Override
    public List<User> findByRoleAndActive(Role role, boolean active) {
        return userJpaRepository.findByRoleAndActive(role, active).stream()
                .map(userEntityMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Long countByRoleAndActive(Role role, boolean active) {
        return userJpaRepository.countByRoleAndActive(role, active);
    }
}