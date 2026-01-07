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
import lombok.extern.slf4j.Slf4j;

@Repository
@RequiredArgsConstructor
@Slf4j
public class UserRepositoryAdapter implements UserRepository {

    private final UserJpaRepository userJpaRepository;
    private final UserEntityMapper userEntityMapper;

    @Override
    public User save(User user) {
        log.info("Saving User email={}, role={}", user.getEmail(), user.getRole());

        UserEntity entity = userEntityMapper.toEntity(user);
        UserEntity saved = userJpaRepository.save(entity);

        log.info("User saved with id={}", saved.getIdUser());
        return userEntityMapper.toDomain(saved);
    }

    @Override
    public Optional<User> findById(Long idUser) {
        log.debug("Finding User by id={}", idUser);

        return userJpaRepository
                .findById(idUser)
                .map(userEntityMapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        log.debug("Finding User by email={}", email);

        return userJpaRepository
                .findByEmail(email)
                .map(userEntityMapper::toDomain);
    }

    @Override
    public Optional<User> findByPhone(String phone) {
        log.debug("Finding User by phone={}", phone);

        return userJpaRepository
                .findByPhone(phone)
                .map(userEntityMapper::toDomain);
    }

    @Override
    public List<User> findAll() {
        log.debug("Finding all Users");

        return userJpaRepository.findAll()
                .stream()
                .map(userEntityMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> findByRole(Role role) {
        log.debug("Finding Users by role={}", role);

        return userJpaRepository.findByRole(role)
                .stream()
                .map(userEntityMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> findByActive(boolean active) {
        log.debug("Finding Users by active={}", active);

        return userJpaRepository.findByActive(active)
                .stream()
                .map(userEntityMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> findAll(int page, int size) {
        log.debug("Finding Users page={}, size={}", page, size);

        return userJpaRepository
                .findAll(PageRequest.of(page, size))
                .stream()
                .map(userEntityMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByEmail(String email) {
        log.debug("Checking existence of User by email={}", email);
        return userJpaRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByPhone(String phone) {
        log.debug("Checking existence of User by phone={}", phone);
        return userJpaRepository.existsByPhone(phone);
    }

    @Override
    public Long count() {
        log.debug("Counting all Users");
        return userJpaRepository.count();
    }

    @Override
    public Long countByRole(Role role) {
        log.debug("Counting Users by role={}", role);
        return userJpaRepository.countByRole(role);
    }

    @Override
    public void delete(User user) {
        if (user.getIdUser() == null) {
            log.warn("Attempt to delete User with null id");
            throw new IllegalArgumentException("User id must not be null");
        }

        log.info("Deleting User with id={}", user.getIdUser());
        UserEntity entity = userEntityMapper.toEntity(user);
        userJpaRepository.delete(entity);
    }

    @Override
    public void deleteById(Long idUser) {
        log.info("Deleting User by id={}", idUser);
        userJpaRepository.deleteById(idUser);
    }

    @Override
    public List<User> findByRoleAndActive(Role role, boolean active) {
        log.debug("Finding Users by role={} and active={}", role, active);

        return userJpaRepository
                .findByRoleAndActive(role, active)
                .stream()
                .map(userEntityMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Long countByRoleAndActive(Role role, boolean active) {
        log.debug("Counting Users by role={} and active={}", role, active);
        return userJpaRepository.countByRoleAndActive(role, active);
    }
}
