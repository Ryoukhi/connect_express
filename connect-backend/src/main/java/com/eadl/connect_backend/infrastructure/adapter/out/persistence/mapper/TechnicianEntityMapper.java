package com.eadl.connect_backend.infrastructure.adapter.out.persistence.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.eadl.connect_backend.domain.model.user.Technician;
import com.eadl.connect_backend.infrastructure.adapter.out.persistence.entity.UserEntity;

public class TechnicianEntityMapper {

	public static UserEntity toEntity(Technician model) {
		if (model == null) return null;
		UserEntity entity = new UserEntity();
		entity.setIdUser(model.getIdUser());
		entity.setFirstName(model.getFirstName());
		entity.setLastName(model.getLastName());
		entity.setEmail(model.getEmail());
		entity.setPhone(model.getPhone());
		entity.setPassword(model.getPassword());
		entity.setRole(model.getRole());
		entity.setCreatedAt(model.getCreatedAt());
		entity.setUpdatedAt(model.getUpdatedAt());
		entity.setActive(model.isActive());
		entity.setEmailVerified(model.isEmailVerified());
		entity.setPhoneVerified(model.isPhoneVerified());
		entity.setProfilePhotoUrl(model.getProfilePhotoUrl());
		return entity;
	}

	public static Technician toModel(UserEntity entity) {
		if (entity == null) return null;
		Technician tech = Technician.create(entity.getFirstName(), entity.getLastName(), entity.getEmail(), entity.getPhone(), entity.getPassword());
		tech.setIdUser(entity.getIdUser());
		tech.setCreatedAt(entity.getCreatedAt());
		tech.setUpdatedAt(entity.getUpdatedAt());
		tech.setProfilePhotoUrl(entity.getProfilePhotoUrl());
		tech.setRole(entity.getRole());
		if (!entity.isActive()) tech.deactivate();
		if (entity.isEmailVerified()) tech.verifyEmail();
		if (entity.isPhoneVerified()) tech.verifyPhone();
		return tech;
	}

	public static List<UserEntity> toEntities(List<Technician> models) {
		if (models == null) return null;
		return models.stream().map(TechnicianEntityMapper::toEntity).collect(Collectors.toList());
	}

	public static List<Technician> toModels(List<UserEntity> entities) {
		if (entities == null) return null;
		return entities.stream().map(TechnicianEntityMapper::toModel).collect(Collectors.toList());
	}

}
