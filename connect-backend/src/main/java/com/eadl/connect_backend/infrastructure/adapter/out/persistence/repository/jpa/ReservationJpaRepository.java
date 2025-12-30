package com.eadl.connect_backend.infrastructure.adapter.out.persistence.repository.jpa;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.eadl.connect_backend.domain.model.reservation.ReservationStatus;
import com.eadl.connect_backend.infrastructure.adapter.out.persistence.entity.ReservationEntity;

public interface ReservationJpaRepository extends JpaRepository<ReservationEntity, Long> {

    List<ReservationEntity> findByClient_IdUser(Long idClient);

    List<ReservationEntity> findByTechnician_IdUser(Long idTechnician);

    List<ReservationEntity> findByStatus(ReservationStatus status);

    boolean existsByTechnician_IdUserAndScheduledTimeBetween(
            Long idTechnician,
            LocalDateTime end,
            LocalDateTime start
    );

    long countByTechnician_IdUserAndStatus(Long idTechnician, ReservationStatus status);

    @Query("""
        select r.review.idReview
        from ReservationEntity r
        where r.technician.idUser = :technicianId
          and r.status = :status
          and r.review is not null
    """)
    List<Long> findReviewIdsByTechnicianAndStatus(Long technicianId, ReservationStatus status);
}
