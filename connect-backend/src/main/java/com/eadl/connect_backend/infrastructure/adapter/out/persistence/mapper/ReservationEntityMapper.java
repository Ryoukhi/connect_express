package com.eadl.connect_backend.infrastructure.adapter.out.persistence.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.eadl.connect_backend.domain.model.reservation.Reservation;
import com.eadl.connect_backend.infrastructure.adapter.out.persistence.entity.ReservationEntity;
import com.eadl.connect_backend.infrastructure.adapter.out.persistence.entity.UserEntity;

public class ReservationEntityMapper {

	public static ReservationEntity toEntity(Reservation model) {
		if (model == null) return null;
		ReservationEntity entity = new ReservationEntity();
		entity.setIdReservation(model.getIdReservation());
		entity.setDateRequested(model.getDateRequested());
		entity.setScheduledTime(model.getScheduledTime());
		entity.setStatus(model.getStatus());
		entity.setPrice(model.getPrice());
		entity.setAddress(model.getAddress());
		entity.setDescription(model.getDescription());
		entity.setCancellationReason(model.getCancellationReason());
		entity.setCompletedAt(model.getCompletedAt());
		entity.setCreatedAt(model.getCreatedAt());
		entity.setUpdatedAt(model.getUpdatedAt());

		if (model.getIdClient() != null) {
			UserEntity client = new UserEntity();
			client.setIdUser(model.getIdClient());
			entity.setClient(client);
		}
		if (model.getIdTechnician() != null) {
			UserEntity tech = new UserEntity();
			tech.setIdUser(model.getIdTechnician());
			entity.setTechnician(tech);
		}

		return entity;
	}

	public static Reservation toModel(ReservationEntity entity) {
		if (entity == null) return null;

		Long clientId = entity.getClient() != null ? entity.getClient().getIdUser() : null;
		Long techId = entity.getTechnician() != null ? entity.getTechnician().getIdUser() : null;

		Reservation model = Reservation.create(clientId, techId,
				entity.getScheduledTime(), entity.getPrice(), entity.getAddress(), entity.getDescription());

		model.setIdReservation(entity.getIdReservation());
		if (entity.getStatus() != null) model.setStatus(entity.getStatus());
		model.setCreatedAt(entity.getCreatedAt());
		model.setUpdatedAt(entity.getUpdatedAt());
		model.setCompletedAt(entity.getCompletedAt());

		return model;
	}

	public static List<ReservationEntity> toEntities(List<Reservation> models) {
		if (models == null) return null;
		return models.stream().map(ReservationEntityMapper::toEntity).collect(Collectors.toList());
	}

	public static List<Reservation> toModels(List<ReservationEntity> entities) {
		if (entities == null) return null;
		return entities.stream().map(ReservationEntityMapper::toModel).collect(Collectors.toList());
	}

}
