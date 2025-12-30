package com.eadl.connect_backend.infrastructure.adapter.out.persistence.mapper;

import com.eadl.connect_backend.domain.model.user.Client;
import com.eadl.connect_backend.infrastructure.adapter.out.persistence.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class ClientEntityMapper {

private final UserEntityMapper userEntityMapper = new UserEntityMapper();

    /**
     * Convertit un Client (domaine) en UserEntity
     */
    public UserEntity toEntity(Client client) {
        if (client == null) return null;

        UserEntity entity = userEntityMapper.toEntity(client);
        entity.setRole(client.getRole()); // assure le rôle CLIENT
        return entity;
    }

    /**
     * Convertit un UserEntity en Client (domaine)
     */
    public Client toDomain(UserEntity entity) {
        if (entity == null) return null;

        Client client = new Client();
        client.setIdUser(entity.getIdUser());
        client.setFirstName(entity.getFirstName());
        client.setLastName(entity.getLastName());
        client.setEmail(entity.getEmail());
        client.setPhone(entity.getPhone());
        client.setPassword(entity.getPassword());
        client.setRole(entity.getRole());
        client.setCity(entity.getCity());
        client.setNeighborhood(entity.getNeighborhood());
        client.setCreatedAt(entity.getCreatedAt());
        client.setUpdatedAt(entity.getUpdatedAt());
        client.setActive(entity.isActive());
        client.setEmailVerified(entity.isEmailVerified());
        client.setPhoneVerified(entity.isPhoneVerified());
        client.setProfilePhotoUrl(entity.getProfilePhotoUrl());

        return client;
    }
}

