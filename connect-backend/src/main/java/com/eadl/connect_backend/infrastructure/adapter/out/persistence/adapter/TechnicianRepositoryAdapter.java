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

@Repository
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
        UserEntity saved = jpaRepository.save(mapper.toEntity(technician));
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Technician> findById(Long idTechnician) {
        return jpaRepository.findById(idTechnician)
                .filter(e -> e.getRole() == Role.TECHNICIAN)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Technician> findByUserId(Long idUser) {
        return jpaRepository
                .findByIdUserAndRole(idUser, Role.TECHNICIAN)
                .map(mapper::toDomain);
    }

    @Override
    public List<Technician> findAll() {
        return jpaRepository.findByRole(Role.TECHNICIAN)
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Long count() {
        return jpaRepository.countByRole(Role.TECHNICIAN);
    }

    @Override
    public void delete(Technician technician) {
        jpaRepository.delete(mapper.toEntity(technician));
    }

    @Override
    public List<Technician> findByActiveTrue() {
        return jpaRepository.findByRoleAndActiveTrue(Role.TECHNICIAN, true)
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Technician> findByRoleAndActiveTrue(Role role, boolean active) {
        // On récupère les entités User correspondant au rôle et au statut actif
        List<UserEntity> entities = jpaRepository.findByRoleAndActiveTrue(role, active);

        // On convertit chaque entity en domaine Technician
        return entities.stream()
                .map(entity -> {
                    User user = mapper.toDomain(entity);
                    if (user instanceof Technician technician) {
                        return technician;
                    }
                    // Si jamais l'entité n'est pas un Technician, on peut filtrer ou lever une exception
                    throw new IllegalStateException("Expected Technician, got " + user.getClass().getSimpleName());
                })
                .toList();
    }

}