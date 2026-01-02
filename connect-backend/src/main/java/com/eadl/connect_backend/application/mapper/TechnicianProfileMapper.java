package com.eadl.connect_backend.application.mapper;

import org.springframework.stereotype.Component;

import com.eadl.connect_backend.application.dto.TechnicianProfileCreateDto;
import com.eadl.connect_backend.application.dto.TechnicianProfileResponseDto;
import com.eadl.connect_backend.application.dto.TechnicianProfileUpdateDto;
import com.eadl.connect_backend.domain.model.technician.TechnicianProfile;

@Component
public class TechnicianProfileMapper {

    public TechnicianProfile toEntity(
            TechnicianProfileCreateDto dto
    ) {
        TechnicianProfile profile = new TechnicianProfile();
        profile.setBio(dto.getBio());
        profile.setIdCategory(dto.getIdCategory());
        profile.setYearsExperience(dto.getYearsExperience());
        profile.setHourlyRate(dto.getHourlyRate());
        return profile;
    }

    public void updateEntity(
            TechnicianProfileUpdateDto dto,
            TechnicianProfile profile
    ) {
        profile.setBio(dto.getBio());
        profile.setIdCategory(dto.getIdCategory());
        profile.setYearsExperience(dto.getYearsExperience());
        profile.setHourlyRate(dto.getHourlyRate());
    }

    public TechnicianProfileResponseDto toDto(
            TechnicianProfile profile
    ) {
        return new TechnicianProfileResponseDto(
                profile.getIdProfile(),
                profile.getIdTechnician(),
                profile.getBio(),
                profile.getIdCategory(),
                profile.getYearsExperience(),
                profile.getHourlyRate(),
                profile.getAvailabilityStatus(),
                profile.isVerified(),
                profile.getCompletedJobs(),
                profile.getAverageRating()
        );
    }
}