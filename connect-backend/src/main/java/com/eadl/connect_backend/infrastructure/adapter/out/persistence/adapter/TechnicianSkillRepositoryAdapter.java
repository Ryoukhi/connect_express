package com.eadl.connect_backend.infrastructure.adapter.out.persistence.adapter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.eadl.connect_backend.domain.model.technician.TechnicianSkill;
import com.eadl.connect_backend.domain.port.out.persistence.TechnicianSkillRepository;
import com.eadl.connect_backend.infrastructure.adapter.out.persistence.entity.TechnicianSkillEntity;
import com.eadl.connect_backend.infrastructure.adapter.out.persistence.mapper.TechnicianSkillEntityMapper;
import com.eadl.connect_backend.infrastructure.adapter.out.persistence.repository.jpa.TechnicianSkillJpaRepository;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class TechnicianSkillRepositoryAdapter implements TechnicianSkillRepository {

    private final TechnicianSkillJpaRepository technicianSkillJpaRepository;
    private final TechnicianSkillEntityMapper mapper;

    public TechnicianSkillRepositoryAdapter(
            TechnicianSkillJpaRepository technicianSkillJpaRepository,
            TechnicianSkillEntityMapper mapper
    ) {
        this.technicianSkillJpaRepository = technicianSkillJpaRepository;
        this.mapper = mapper;
    }

    @Override
    public TechnicianSkill save(TechnicianSkill skill) {
        log.info("Saving TechnicianSkill for userId={}", skill.getIdUser());

        TechnicianSkillEntity entity = mapper.toEntity(skill);
        TechnicianSkillEntity saved = technicianSkillJpaRepository.save(entity);

        log.info("TechnicianSkill saved with id={}", saved.getIdSkill());
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<TechnicianSkill> findById(Long skillId) {
        log.debug("Finding TechnicianSkill by id={}", skillId);

        return technicianSkillJpaRepository.findById(skillId)
                .map(mapper::toDomain);
    }

    @Override
    public List<TechnicianSkill> findByUserId(Long userId) {
        log.debug("Finding TechnicianSkills by userId={} (fallback via findAll)", userId);

        return technicianSkillJpaRepository.findAll().stream()
                .filter(e -> e.getTechnician() != null && userId.equals(e.getTechnician().getIdUser()))
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<TechnicianSkill> findByUserIdAndCategoryId(
            Long userId,
            Long categoryId
    ) {
        log.debug(
                "Finding TechnicianSkills by userId={} and categoryId={} (fallback via findAll)",
                userId, categoryId
        );

        return technicianSkillJpaRepository.findAll().stream()
                .filter(e -> e.getTechnician() != null && userId.equals(e.getTechnician().getIdUser())
                        && e.getCategory() != null && categoryId.equals(e.getCategory().getIdCategory()))
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByUserIdAndName(Long userId, String name) {
        log.debug(
                "Checking existence of TechnicianSkill userId={}, name={} (fallback via findAll)",
                userId, name
        );

        return technicianSkillJpaRepository.findAll().stream()
                .filter(e -> e.getTechnician() != null && userId.equals(e.getTechnician().getIdUser()))
                .anyMatch(e -> name != null && name.equalsIgnoreCase(e.getName()));
    }

    @Override
    public void delete(TechnicianSkill skill) {
        if (skill.getIdSkill() == null) {
            log.warn("Attempt to delete TechnicianSkill with null id");
            throw new IllegalArgumentException("TechnicianSkill id must not be null");
        }

        log.info("Deleting TechnicianSkill with id={}", skill.getIdSkill());
        technicianSkillJpaRepository.delete(mapper.toEntity(skill));
    }

    @Override
    public void deleteByUserId(Long userId) {
        log.info("Deleting all TechnicianSkills for userId={} (fallback)", userId);
        technicianSkillJpaRepository.findAll().stream()
                .filter(e -> e.getTechnician() != null && userId.equals(e.getTechnician().getIdUser()))
                .forEach(technicianSkillJpaRepository::delete);
    }

    @Override
    public List<TechnicianSkill> findAll() {
        log.debug("Retrieving all TechnicianSkills via JPA");
        return technicianSkillJpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<TechnicianSkill> findByCategoryId(Long categoryId) {
        log.debug("Finding TechnicianSkills by categoryId={}", categoryId);

        return technicianSkillJpaRepository
                .findByCategory_IdCategory(categoryId)
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}
