package com.eadl.connect_backend.application.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.eadl.connect_backend.application.dto.TechnicianSkillDto;
import com.eadl.connect_backend.domain.model.technician.TechnicianSkill;

public class TechnicianSkillMapper {

    private TechnicianSkillMapper() {
        // Utility class
    }

    public static TechnicianSkillDto toDto(TechnicianSkill skill) {
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

    public static List<TechnicianSkillDto> toDtoList(List<TechnicianSkill> skills) {
        return skills.stream()
                .map(TechnicianSkillMapper::toDto)
                .collect(Collectors.toList());
    }
}