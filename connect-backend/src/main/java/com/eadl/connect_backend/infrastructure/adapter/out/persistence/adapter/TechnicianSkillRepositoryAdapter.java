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

@Repository
public class TechnicianSkillRepositoryAdapter implements TechnicianSkillRepository {
    private final TechnicianSkillJpaRepository technicianSkillJpaRepository;
    private final TechnicianSkillEntityMapper mapper;

   

    public TechnicianSkillRepositoryAdapter(TechnicianSkillJpaRepository technicianSkillJpaRepository,
            TechnicianSkillEntityMapper mapper) {
        this.technicianSkillJpaRepository = technicianSkillJpaRepository;
        this.mapper = mapper;
    }

    @Override
    public TechnicianSkill save(TechnicianSkill skill) {
        TechnicianSkillEntity entity = mapper.toEntity(skill);
        TechnicianSkillEntity saved = technicianSkillJpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<TechnicianSkill> findById(Long skillId) {
        return technicianSkillJpaRepository.findById(skillId)
                .map(mapper::toDomain);
    }

    @Override
    public List<TechnicianSkill> findByProfileId(Long profileId) {
        return technicianSkillJpaRepository.findByProfile_IdProfile(profileId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<TechnicianSkill> findByProfileIdAndCategoryId(Long profileId, Long categoryId) {
        return technicianSkillJpaRepository.findByProfile_IdProfileAndCategory_IdCategory(profileId, categoryId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByProfileIdAndNameSkill(Long profileId, String nameSkill) {
        return technicianSkillJpaRepository.existsByProfile_IdProfileAndName(profileId, nameSkill);
    }

    @Override
    public void delete(TechnicianSkill skill) {
        technicianSkillJpaRepository.delete(mapper.toEntity(skill));
    }

    @Override
    public void deleteByProfileId(Long profileId) {
        technicianSkillJpaRepository.deleteByProfile_IdProfile(profileId);
    }

    @Override
    public List<TechnicianSkill> findByCategoryId(Long categoryId) {
        return technicianSkillJpaRepository.findByCategory_IdCategory(categoryId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}