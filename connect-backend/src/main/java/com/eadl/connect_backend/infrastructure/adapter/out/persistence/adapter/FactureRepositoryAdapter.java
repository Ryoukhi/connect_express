package com.eadl.connect_backend.infrastructure.adapter.out.persistence.adapter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.eadl.connect_backend.domain.model.payment.Facture;
import com.eadl.connect_backend.domain.port.out.persistence.FactureRepository;
import com.eadl.connect_backend.infrastructure.adapter.out.persistence.entity.FactureEntity;
import com.eadl.connect_backend.infrastructure.adapter.out.persistence.mapper.FactureEntityMapper;
import com.eadl.connect_backend.infrastructure.adapter.out.persistence.repository.jpa.FactureJpaRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
@Transactional
public class FactureRepositoryAdapter implements FactureRepository {

    private static final Logger log = LoggerFactory.getLogger(FactureRepositoryAdapter.class);

    private final FactureEntityMapper factureEntityMapper;
    private final FactureJpaRepository factureJpaRepository;

    @Override
    public Facture save(Facture facture) {
        log.info("Saving facture: {}", facture);

        FactureEntity entity = factureEntityMapper.toEntity(facture);
        FactureEntity saved = factureJpaRepository.save(entity);
        Facture savedFacture = factureEntityMapper.toModel(saved);

        log.info("Saved facture: {}", savedFacture);
        return savedFacture;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Facture> findById(Long idFacture) {
        log.info("Finding facture by id: {}", idFacture);

        Optional<Facture> facture = factureJpaRepository.findById(idFacture)
                .map(factureEntityMapper::toModel);

        facture.ifPresentOrElse(
                f -> log.info("Found facture: {}", f),
                () -> log.warn("No facture found with id: {}", idFacture)
        );

        return facture;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Facture> findAll() {
        log.info("Fetching all factures");

        List<Facture> factures = factureJpaRepository.findAll()
                .stream()
                .map(factureEntityMapper::toModel)
                .collect(Collectors.toList());

        log.info("Found {} factures", factures.size());
        return factures;
    }

    @Override
    public void deleteById(Long idFacture) {
        log.info("Deleting facture by id: {}", idFacture);
        factureJpaRepository.deleteById(idFacture);
        log.info("Facture with id {} deleted", idFacture);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Facture> findByInvoiceNumber(String invoiceNumber) {
        log.info("Finding facture by invoice number: {}", invoiceNumber);

        Optional<Facture> facture = factureJpaRepository.findByInvoiceNumber(invoiceNumber)
                .map(factureEntityMapper::toModel);

        facture.ifPresentOrElse(
                f -> log.info("Found facture: {}", f),
                () -> log.warn("No facture found with invoice number: {}", invoiceNumber)
        );

        return facture;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Facture> findByReservationId(Long idReservation) {
        log.info("Finding facture by reservation id: {}", idReservation);

        Optional<Facture> facture = factureJpaRepository.findByReservation_IdReservation(idReservation)
                .map(factureEntityMapper::toModel);

        facture.ifPresentOrElse(
                f -> log.info("Found facture: {}", f),
                () -> log.warn("No facture found for reservation id: {}", idReservation)
        );

        return facture;
    }

    @Override
    public void delete(Facture facture) {
        log.info("Deleting facture: {}", facture);

        FactureEntity entity = factureEntityMapper.toEntity(facture);
        factureJpaRepository.delete(entity);

        log.info("Deleted facture: {}", facture);
    }
}
