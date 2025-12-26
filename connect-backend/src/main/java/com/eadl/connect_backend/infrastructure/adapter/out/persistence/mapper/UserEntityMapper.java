package com.eadl.connect_backend.infrastructure.adapter.out.persistence.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.eadl.connect_backend.domain.model.user.Admin;
import com.eadl.connect_backend.domain.model.user.Client;
import com.eadl.connect_backend.domain.model.user.Role;
import com.eadl.connect_backend.domain.model.user.Technician;
import com.eadl.connect_backend.domain.model.user.User;
import com.eadl.connect_backend.infrastructure.adapter.out.persistence.entity.UserEntity;

public class UserEntityMapper {

	public static UserEntity toEntity(User model) {
		if (model == null) return null;
		UserEntity entity = new UserEntity();
		entity.setIdUser(model.getIdUser());
		entity.setFirstName(model.getFirstName());
		entity.setLastName(model.getLastName());
		entity.setEmail(model.getEmail());
		entity.setPhone(model.getPhone());
		entity.setPassword(model.getPassword());
		entity.setRole(model.getRole() != null ? model.getRole() : Role.CLIENT);
		entity.setCreatedAt(model.getCreatedAt());
		entity.setUpdatedAt(model.getUpdatedAt());
		entity.setActive(model.isActive());
		entity.setEmailVerified(model.isEmailVerified());
		entity.setPhoneVerified(model.isPhoneVerified());
		entity.setProfilePhotoUrl(model.getProfilePhotoUrl());
		return entity;
	}

	public static User toModel(UserEntity entity) {
		if (entity == null) return null;

		Role role = entity.getRole();
		User user;
		if (role == Role.ADMIN) {
			Admin admin = Admin.create(entity.getFirstName(), entity.getLastName(), entity.getEmail(), entity.getPhone(), entity.getPassword());
			user = admin;
		} else if (role == Role.TECHNICIAN) {
			Technician tech = Technician.create(entity.getFirstName(), entity.getLastName(), entity.getEmail(), entity.getPhone(), entity.getPassword());
			user = tech;
		} else { // default to client
			Client client = Client.create(entity.getFirstName(), entity.getLastName(), entity.getEmail(), entity.getPhone(), entity.getPassword(), null);
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

	public static List<UserEntity> toEntities(List<User> models) {
		if (models == null) return null;
		return models.stream().map(UserEntityMapper::toEntity).collect(Collectors.toList());
	}

	public static List<User> toModels(List<UserEntity> entities) {
		if (entities == null) return null;
		return entities.stream().map(UserEntityMapper::toModel).collect(Collectors.toList());
	}

}
