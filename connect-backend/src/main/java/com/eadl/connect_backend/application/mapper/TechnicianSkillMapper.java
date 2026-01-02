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
        technicianSkillDto.setIdProfile(skill.getIdProfile());
        technicianSkillDto.setIdCategory(skill.getIdCategory());
        technicianSkillDto.setNameSkill(skill.getNameSkill());
        technicianSkillDto.setDescription(skill.getDescription());
        technicianSkillDto.setLevel(skill.getLevel());

        return technicianSkillDto;
    }

    public TechnicianSkill toModel(TechnicianSkillDto dto) { 
        if (dto == null) return null;

        TechnicianSkill skill = new TechnicianSkill();
        
        skill.setIdSkill(dto.getIdSkill());
        skill.setIdProfile(dto.getIdProfile());
        skill.setIdCategory(dto.getIdCategory());
        skill.setNameSkill(dto.getNameSkill());
        skill.setDescription(dto.getDescription());
        skill.setLevel(dto.getLevel());

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