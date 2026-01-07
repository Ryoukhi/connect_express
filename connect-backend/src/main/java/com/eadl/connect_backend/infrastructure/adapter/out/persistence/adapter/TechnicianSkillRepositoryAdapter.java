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
        log.info("Saving TechnicianSkill for profileId={}", skill.getIdProfile());

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
    public List<TechnicianSkill> findByProfileId(Long profileId) {
        log.debug("Finding TechnicianSkills by profileId={}", profileId);

        return technicianSkillJpaRepository
                .findByProfile_IdProfile(profileId)
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<TechnicianSkill> findByProfileIdAndCategoryId(
            Long profileId,
            Long categoryId
    ) {
        log.debug(
                "Finding TechnicianSkills by profileId={} and categoryId={}",
                profileId, categoryId
        );

        return technicianSkillJpaRepository
                .findByProfile_IdProfileAndCategory_IdCategory(profileId, categoryId)
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByProfileIdAndNameSkill(Long profileId, String nameSkill) {
        log.debug(
                "Checking existence of TechnicianSkill profileId={}, name={}",
                profileId, nameSkill
        );

        return technicianSkillJpaRepository
                .existsByProfile_IdProfileAndName(profileId, nameSkill);
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
    public void deleteByProfileId(Long profileId) {
        log.info("Deleting all TechnicianSkills for profileId={}", profileId);
        technicianSkillJpaRepository.deleteByProfile_IdProfile(profileId);
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
