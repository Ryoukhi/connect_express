package com.eadl.connect_backend.infrastructure.adapter.out.persistence.repository.jpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eadl.connect_backend.infrastructure.adapter.out.persistence.entity.FactureEntity;

public interface FactureJpaRepository extends JpaRepository<FactureEntity, Long> {

    /**
     * Récupère une facture par numéro
     */
    Optional<FactureEntity> findByInvoiceNumber(String invoiceNumber);
    
    /**
     * Récupère la facture d'une réservation
     */
    Optional<FactureEntity> findByReservation_IdReservation(Long idReservation);


}
