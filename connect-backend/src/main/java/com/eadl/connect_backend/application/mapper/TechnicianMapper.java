package com.eadl.connect_backend.application.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.eadl.connect_backend.application.dto.response.profile.TechnicianDocumentResponse;
import com.eadl.connect_backend.application.dto.response.profile.TechnicianProfileResponse;
import com.eadl.connect_backend.application.dto.response.profile.TechnicianSkillResponse;
import com.eadl.connect_backend.domain.model.technician.TechnicianDocument;
import com.eadl.connect_backend.domain.model.technician.TechnicianProfile;
import com.eadl.connect_backend.domain.model.technician.TechnicianSkill;

/**
 * Mapper utilitaire pour les objets liés aux techniciens (profil, compétences, documents)
 */
public class TechnicianMapper {

    public static TechnicianProfileResponse toProfileResponse(TechnicianProfile profile,
                                                              List<TechnicianSkill> skills,
                                                              List<TechnicianDocument> documents) {
        if (profile == null) return null;
        TechnicianProfileResponse dto = new TechnicianProfileResponse();
        dto.setIdProfile(profile.getIdProfile());
        dto.setIdTechnician(profile.getIdTechnician());
        dto.setBio(profile.getBio());
        dto.setYearsExperience(profile.getYearsExperience());
        dto.setHourlyRate(profile.getHourlyRate());
        dto.setVerified(profile.getVerified());
        dto.setLatitude(profile.getLatitude());
        dto.setLongitude(profile.getLongitude());
        dto.setAvailabilityStatus(profile.getAvailabilityStatus() != null ? profile.getAvailabilityStatus().name() : null);
        dto.setProfilePhotoUrl(profile.getProfilePhotoUrl());
        dto.setCompletedJobs(profile.getCompletedJobs());
        dto.setAverageRating(profile.getAverageRating());
        if (skills != null) {
            dto.setSkills(skills.stream().map(TechnicianMapper::toSkillResponse).collect(Collectors.toList()));
        }
        // Documents are not part of TechnicianProfileResponse by default; if needed they can be added elsewhere
        // Return DTO
        return dto;
    }

    public static TechnicianSkillResponse toSkillResponse(TechnicianSkill skill) {
        if (skill == null) return null;
        TechnicianSkillResponse dto = new TechnicianSkillResponse();
        dto.setIdSkill(skill.getIdSkill());
        dto.setIdCategory(skill.getIdCategory());
        // categoryName not resolved here; resolver should set it if available
        dto.setSkillName(skill.getNameSkill());
        dto.setDescription(skill.getDescription());
        dto.setLevel(skill.getLevel());
        return dto;
    }

    public static TechnicianDocumentResponse toDocumentResponse(TechnicianDocument doc) {
        if (doc == null) return null;
        TechnicianDocumentResponse dto = new TechnicianDocumentResponse();
        dto.setIdDocument(doc.getIdDocument());
        dto.setType(doc.getType() != null ? doc.getType().name() : null);
        dto.setUrl(doc.getUrl());
        dto.setUploadedAt(doc.getUploadedAt());
        dto.setVerified(doc.isVerified());
        dto.setVerificationNote(doc.getVerificationNote());
        return dto;
    }

    public static List<TechnicianDocumentResponse> toDocumentResponses(List<TechnicianDocument> documents) {
        if (documents == null) return null;
        return documents.stream().map(TechnicianMapper::toDocumentResponse).collect(Collectors.toList());
    }

    public static List<TechnicianSkillResponse> toSkillResponses(List<TechnicianSkill> skills) {
        if (skills == null) return null;
        return skills.stream().map(TechnicianMapper::toSkillResponse).collect(Collectors.toList());
    }
}
