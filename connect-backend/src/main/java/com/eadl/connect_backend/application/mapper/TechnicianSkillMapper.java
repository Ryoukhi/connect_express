package com.eadl.connect_backend.application.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.eadl.connect_backend.application.dto.TechnicianSkillDto;
import com.eadl.connect_backend.domain.model.technician.TechnicianSkill;

@Component
public class TechnicianSkillMapper {

    private TechnicianSkillMapper() {
        // Utility class
    }

    public TechnicianSkillDto toDto(TechnicianSkill skill) {
        if (skill == null) return null;

        TechnicianSkillDto technicianSkillDto = new TechnicianSkillDto();
        
        technicianSkillDto.setIdSkill(skill.getIdSkill());
        technicianSkillDto.setIdUser(skill.getIdUser());
        technicianSkillDto.setIdCategory(skill.getIdCategory());
        technicianSkillDto.setName(skill.getName());
        technicianSkillDto.setDescription(skill.getDescription());
        technicianSkillDto.setLevel(skill.getLevel());
        technicianSkillDto.setYearsExperience(skill.getYearsExperience());
        technicianSkillDto.setHourlyRate(skill.getHourlyRate());
        technicianSkillDto.setAvailabilityStatus(skill.getAvailabilityStatus());
        technicianSkillDto.setVerified(skill.isVerified());
        technicianSkillDto.setCreatedAt(skill.getCreatedAt());
        technicianSkillDto.setUpdatedAt(skill.getUpdatedAt());

        return technicianSkillDto;
    }

    public TechnicianSkill toModel(TechnicianSkillDto dto) { 
        if (dto == null) return null;

        TechnicianSkill skill = new TechnicianSkill();
        
        skill.setIdSkill(dto.getIdSkill());
        skill.setIdUser(dto.getIdUser());
        skill.setIdCategory(dto.getIdCategory());
        skill.setName(dto.getName());
        skill.setDescription(dto.getDescription());
        skill.setLevel(dto.getLevel());
        skill.setYearsExperience(dto.getYearsExperience());
        skill.setHourlyRate(dto.getHourlyRate());
        skill.setAvailabilityStatus(dto.getAvailabilityStatus());
        skill.setVerified(dto.isVerified());
        skill.setCreatedAt(dto.getCreatedAt());
        skill.setUpdatedAt(dto.getUpdatedAt());

        return skill;
    }

    public List<TechnicianSkillDto> toDtoList(List<TechnicianSkill> skills) {
        return skills.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<TechnicianSkill> toModelList(List<TechnicianSkillDto> dtos) {
        return dtos.stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }
}