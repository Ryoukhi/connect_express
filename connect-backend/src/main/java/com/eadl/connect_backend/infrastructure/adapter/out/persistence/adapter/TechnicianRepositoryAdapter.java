package com.eadl.connect_backend.infrastructure.adapter.out.persistence.adapter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.eadl.connect_backend.domain.model.user.Role;
import com.eadl.connect_backend.domain.model.user.Technician;
import com.eadl.connect_backend.domain.model.user.User;
import com.eadl.connect_backend.domain.port.out.persistence.TechnicianRepository;
import com.eadl.connect_backend.infrastructure.adapter.out.persistence.entity.UserEntity;
import com.eadl.connect_backend.infrastructure.adapter.out.persistence.mapper.TechnicianEntityMapper;
import com.eadl.connect_backend.infrastructure.adapter.out.persistence.repository.jpa.TechnicianJpaRepository;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class TechnicianRepositoryAdapter implements TechnicianRepository {

    private final TechnicianJpaRepository jpaRepository;
    private final TechnicianEntityMapper mapper;

    public TechnicianRepositoryAdapter(
            TechnicianJpaRepository jpaRepository,
            TechnicianEntityMapper mapper
    ) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Technician save(Technician technician) {
        log.info("Saving Technician");

        UserEntity saved = jpaRepository.save(mapper.toEntity(technician));

        log.info("Technician saved with id={}", saved.getIdUser());
        return mapper.toDomain(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Technician> findById(Long idTechnician) {
        log.debug("Finding Technician by id={}", idTechnician);

        return jpaRepository.findById(idTechnician)
                .filter(e -> e.getRole() == Role.TECHNICIAN)
                .map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Technician> findByidUser(Long idUser) {
        log.debug("Finding Technician by idUser={}", idUser);

        return jpaRepository
                .findByIdUserAndRole(idUser, Role.TECHNICIAN)
                .map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Technician> findAll() {
        log.debug("Finding all Technicians");

        return jpaRepository.findByRole(Role.TECHNICIAN)
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Long count() {
        log.debug("Counting Technicians");

        return jpaRepository.countByRole(Role.TECHNICIAN);
    }

    @Override
    public void delete(Technician technician) {
        if (technician.getIdUser() == null) {
            log.warn("Attempt to delete Technician with null id");
            throw new IllegalArgumentException("Technician id must not be null");
        }

        log.info("Deleting Technician with id={}", technician.getIdUser());
        jpaRepository.delete(mapper.toEntity(technician));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Technician> findByActiveTrue() {
        log.debug("Finding active Technicians");

        return jpaRepository
                .findByRoleAndActiveTrue(Role.TECHNICIAN, true)
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Technician> findByRoleAndActiveTrue(Role role, boolean active) {
        log.debug(
                "Finding users by role={} and active={}",
                role, active
        );

        List<UserEntity> entities =
                jpaRepository.findByRoleAndActiveTrue(role, active);

        return entities.stream()
                .map(entity -> {
                    User user = mapper.toDomain(entity);

                    if (user instanceof Technician technician) {
                        return technician;
                    }

                    log.error(
                            "Expected Technician but got {} for userId={}",
                            user.getClass().getSimpleName(),
                            entity.getIdUser()
                    );

                    throw new IllegalStateException(
                            "Expected Technician, got " + user.getClass().getSimpleName()
                    );
                })
                .toList();
    }
}
