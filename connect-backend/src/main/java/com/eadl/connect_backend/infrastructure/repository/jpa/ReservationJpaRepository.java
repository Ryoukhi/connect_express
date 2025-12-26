package com.eadl.connect_backend.infrastructure.repository.jpa;

import com.eadl.connect_backend.domain.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository Spring Data JPA pour l'entité Reservation
 */
@Repository
public interface ReservationJpaRepository extends JpaRepository<Reservation, Long> {

}