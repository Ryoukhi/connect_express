package com.eadl.connect_backend.infrastructure.adapter.out.persistence.mapper;

import java.util.List;
import com.eadl.connect_backend.domain.model.user.Admin;
import com.eadl.connect_backend.domain.model.user.Client;
import com.eadl.connect_backend.domain.model.user.Technician;
import com.eadl.connect_backend.domain.model.user.User;
import com.eadl.connect_backend.infrastructure.adapter.out.persistence.entity.UserEntity;

public class UserEntityMapper {

    public UserEntity toEntity(User user) {
        if (user == null) return null;

        UserEntity entity = new UserEntity();
        entity.setIdUser(user.getIdUser());
        entity.setFirstName(user.getFirstName());
        entity.setLastName(user.getLastName());
        entity.setEmail(user.getEmail());
        entity.setPhone(user.getPhone());
        entity.setPassword(user.getPassword());
        entity.setRole(user.getRole());
        entity.setCreatedAt(user.getCreatedAt());
        entity.setUpdatedAt(user.getUpdatedAt());
        entity.setActive(user.isActive());
        entity.setEmailVerified(user.isEmailVerified());
        entity.setPhoneVerified(user.isPhoneVerified());
        entity.setProfilePhotoUrl(user.getProfilePhotoUrl());
        return entity;
    }

    public User toDomain(UserEntity entity) {
        if (entity == null) return null;

        User user;
        switch (entity.getRole()) {
            case ADMIN -> user = new Admin();
            case TECHNICIAN -> user = new Technician();
            default -> user = new Client();
        }

        user.restore(
            entity.getIdUser(),
            entity.getFirstName(),
            entity.getLastName(),
            entity.getEmail(),
            entity.getPhone(),
            entity.getPassword(),
            entity.getRole(),
            entity.getCreatedAt(),
            entity.getUpdatedAt(),
            entity.isActive(),
            entity.isEmailVerified(),
            entity.isPhoneVerified(),
            entity.getProfilePhotoUrl()
        );

        return user;
    }

    public List<UserEntity> toEntities(List<User> users) {
        if (users == null) return List.of();
        return users.stream().map(this::toEntity).toList();
    }

    public List<User> toDomains(List<UserEntity> entities) {
        if (entities == null) return List.of();
        return entities.stream().map(this::toDomain).toList();
    }

    public UserEntity toEntityIdOnly(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User id must not be null");
        }

        UserEntity entity = new UserEntity();
        entity.setIdUser(userId);
        return entity;
    }
}
