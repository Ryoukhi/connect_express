package com.eadl.connect_backend.infrastructure.adapter.out.persistence.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.eadl.connect_backend.domain.model.user.Admin;
import com.eadl.connect_backend.domain.model.user.Client;
import com.eadl.connect_backend.domain.model.user.Role;
import com.eadl.connect_backend.domain.model.user.Technician;
import com.eadl.connect_backend.domain.model.user.User;
import com.eadl.connect_backend.infrastructure.adapter.out.persistence.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserEntityMapper {

    /**
     * Convertit un User (domaine) en UserEntity
     */
    public UserEntity toEntity(User user) {
        if (user == null) return null;

        UserEntity entity = new UserEntity();
        entity.setIdUser(user.getIdUser());
        entity.setFirstName(user.getFirstName());
        entity.setLastName(user.getLastName());
        entity.setEmail(user.getEmail());
        entity.setPhone(user.getPhone());
        entity.setPassword(user.getPassword());
        entity.setRole(user.getRole() != null ? user.getRole() : Role.CLIENT);
        entity.setCity(user.getCity());
        entity.setNeighborhood(user.getNeighborhood());
        entity.setCreatedAt(user.getCreatedAt());
        entity.setUpdatedAt(user.getUpdatedAt());
        entity.setActive(user.isActive());
        entity.setEmailVerified(user.isEmailVerified());
        entity.setPhoneVerified(user.isPhoneVerified());
        entity.setProfilePhotoUrl(user.getProfilePhotoUrl());

        return entity;
    }

    /**
     * Convertit un UserEntity en User (domaine)
     */
    public User toDomain(UserEntity entity) {
        if (entity == null) return null;

        Role role = entity.getRole();
        User user;

        // Spécialisation selon le rôle
        if (role == Role.ADMIN) {
            Admin admin = Admin.create(
                    entity.getFirstName(),
                    entity.getLastName(),
                    entity.getEmail(),
                    entity.getPhone(),
                    entity.getPassword()
            );
            user = admin;
        } else if (role == Role.TECHNICIAN) {
            Technician tech = Technician.create(
                    entity.getFirstName(),
                    entity.getLastName(),
                    entity.getEmail(),
                    entity.getPhone(),
                    entity.getPassword()
            );
            user = tech;
        } else { // CLIENT par défaut
            Client client = Client.create(
                    entity.getFirstName(),
                    entity.getLastName(),
                    entity.getEmail(),
                    entity.getPhone(),
                    entity.getPassword(),
                    entity.getCity()
            );
            client.setNeighborhood(entity.getNeighborhood());
            user = client;
        }

        user.setIdUser(entity.getIdUser());
        user.setCreatedAt(entity.getCreatedAt());
        user.setUpdatedAt(entity.getUpdatedAt());
        user.setProfilePhotoUrl(entity.getProfilePhotoUrl());

        if (entity.isActive()) user.activate(); else user.deactivate();
        if (entity.isEmailVerified()) user.verifyEmail();
        if (entity.isPhoneVerified()) user.verifyPhone();

        return user;
    }

    /**
     * Convertit une liste de Users en UserEntities
     */
    public List<UserEntity> toEntities(List<User> users) {
        if (users == null) return null;
        return users.stream().map(this::toEntity).collect(Collectors.toList());
    }

    /**
     * Convertit une liste de UserEntities en Users
     */
    public List<User> toDomains(List<UserEntity> entities) {
        if (entities == null) return null;
        return entities.stream().map(this::toDomain).collect(Collectors.toList());
    }

    /**
     * Conversion "ID only" (utile pour relation)
     */
    public UserEntity toEntityIdOnly(Long userId) {
        if (userId == null) return null;
        UserEntity entity = new UserEntity();
        entity.setIdUser(userId);
        return entity;
    }
}