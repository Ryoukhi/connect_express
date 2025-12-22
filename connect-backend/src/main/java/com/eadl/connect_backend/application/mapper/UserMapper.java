package com.eadl.connect_backend.application.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.eadl.connect_backend.application.dto.response.profile.TechnicianProfileResponse;
import com.eadl.connect_backend.application.dto.response.user.ClientResponse;
import com.eadl.connect_backend.application.dto.response.user.TechnicianResponse;
import com.eadl.connect_backend.application.dto.response.user.UserResponse;
import com.eadl.connect_backend.domain.model.technician.TechnicianProfile;
import com.eadl.connect_backend.domain.model.user.Client;
import com.eadl.connect_backend.domain.model.user.Technician;
import com.eadl.connect_backend.domain.model.user.User;

/**
 * Mapper utilitaire pour convertir les entités User (et spécialisations)
 * en DTOs de réponse.
 */
public class UserMapper {

    public static UserResponse toUserResponse(User user) {
        if (user == null) return null;
        UserResponse dto = new UserResponse();
        dto.setIdUser(user.getIdUser());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setRole(user.getRole() != null ? user.getRole().name() : null);
        dto.setActive(user.isActive());
        dto.setEmailVerified(user.isEmailVerified());
        dto.setPhoneVerified(user.isPhoneVerified());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        return dto;
    }

    public static ClientResponse toClientResponse(Client client) {
        if (client == null) return null;
        ClientResponse dto = new ClientResponse();
        UserResponse base = toUserResponse(client);
        dto.setIdUser(base.getIdUser());
        dto.setFirstName(base.getFirstName());
        dto.setLastName(base.getLastName());
        dto.setFullName(base.getFullName());
        dto.setEmail(base.getEmail());
        dto.setPhone(base.getPhone());
        dto.setRole(base.getRole());
        dto.setActive(base.isActive());
        dto.setEmailVerified(base.isEmailVerified());
        dto.setPhoneVerified(base.isPhoneVerified());
        dto.setCreatedAt(base.getCreatedAt());
        dto.setUpdatedAt(base.getUpdatedAt());
        dto.setAddress(client.getAddress());
        return dto;
    }

    public static TechnicianResponse toTechnicianResponse(Technician technician) {
        return toTechnicianResponse(technician, null);
    }

    public static TechnicianResponse toTechnicianResponse(Technician technician, TechnicianProfile profile) {
        if (technician == null) return null;
        TechnicianResponse dto = new TechnicianResponse();
        UserResponse base = toUserResponse(technician);
        dto.setIdUser(base.getIdUser());
        dto.setFirstName(base.getFirstName());
        dto.setLastName(base.getLastName());
        dto.setFullName(base.getFullName());
        dto.setEmail(base.getEmail());
        dto.setPhone(base.getPhone());
        dto.setRole(base.getRole());
        dto.setActive(base.isActive());
        dto.setEmailVerified(base.isEmailVerified());
        dto.setPhoneVerified(base.isPhoneVerified());
        dto.setCreatedAt(base.getCreatedAt());
        dto.setUpdatedAt(base.getUpdatedAt());
        if (profile != null) {
            dto.setProfile(mapProfile(profile));
        }
        return dto;
    }

    public static List<UserResponse> toUserResponses(List<User> users) {
        if (users == null) return null;
        return users.stream().map(UserMapper::toUserResponse).collect(Collectors.toList());
    }

    private static TechnicianProfileResponse mapProfile(TechnicianProfile profile) {
        if (profile == null) return null;
        TechnicianProfileResponse r = new TechnicianProfileResponse();
        r.setIdProfile(profile.getIdProfile());
        r.setIdTechnician(profile.getIdTechnician());
        r.setBio(profile.getBio());
        r.setYearsExperience(profile.getYearsExperience());
        r.setHourlyRate(profile.getHourlyRate());
        r.setVerified(profile.getVerified());
        r.setLatitude(profile.getLatitude());
        r.setLongitude(profile.getLongitude());
        r.setAvailabilityStatus(profile.getAvailabilityStatus() != null ? profile.getAvailabilityStatus().name() : null);
        r.setLastLocationUpdate(profile.getLastLocationUpdate());
        r.setProfilePhotoUrl(profile.getProfilePhotoUrl());
        r.setCompletedJobs(profile.getCompletedJobs());
        r.setAverageRating(profile.getAverageRating());
        // skills mapping not implemented here (if needed, add a SkillsMapper)
        return r;
    }
}

