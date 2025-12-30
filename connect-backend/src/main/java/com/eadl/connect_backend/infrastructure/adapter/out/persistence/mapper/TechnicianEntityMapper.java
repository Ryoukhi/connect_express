package com.eadl.connect_backend.infrastructure.adapter.out.persistence.mapper;

import com.eadl.connect_backend.domain.model.user.Technician;
import com.eadl.connect_backend.infrastructure.adapter.out.persistence.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class TechnicianEntityMapper {

	
private final UserEntityMapper userEntityMapper = new UserEntityMapper();

    /**
     * Convertit un Technician (domaine) en UserEntity
     */
    public UserEntity toEntity(Technician technician) {
        if (technician == null) return null;

        UserEntity entity = userEntityMapper.toEntity(technician);
        entity.setRole(technician.getRole()); // assure le rôle TECHNICIAN
        return entity;
    }

    /**
     * Convertit un UserEntity en Technician (domaine)
     */
    public Technician toDomain(UserEntity entity) {
        if (entity == null) return null;

        Technician technician = new Technician();
        technician.setIdUser(entity.getIdUser());
        technician.setFirstName(entity.getFirstName());
        technician.setLastName(entity.getLastName());
        technician.setEmail(entity.getEmail());
        technician.setPhone(entity.getPhone());
        technician.setPassword(entity.getPassword());
        technician.setRole(entity.getRole());
        technician.setCity(entity.getCity());
        technician.setNeighborhood(entity.getNeighborhood());
        technician.setCreatedAt(entity.getCreatedAt());
        technician.setUpdatedAt(entity.getUpdatedAt());
        technician.setActive(entity.isActive());
        technician.setEmailVerified(entity.isEmailVerified());
        technician.setPhoneVerified(entity.isPhoneVerified());
        technician.setProfilePhotoUrl(entity.getProfilePhotoUrl());

        return technician;
    }
}