package com.eadl.connect_backend.infrastructure.adapter.out.persistence.adapter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    private final FactureEntityMapper factureEntityMapper;
    private final FactureJpaRepository factureJpaRepository;

    @Override
    public Facture save(Facture facture) {
        FactureEntity entity = factureEntityMapper.toEntity(facture);
        FactureEntity saved = factureJpaRepository.save(entity);
        return factureEntityMapper.toModel(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Facture> findById(Long idFacture) {
        return factureJpaRepository.findById(idFacture)
                .map(factureEntityMapper::toModel);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Facture> findAll() {
        return factureJpaRepository.findAll()
                .stream()
                .map(factureEntityMapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long idFacture) {
        factureJpaRepository.deleteById(idFacture);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Facture> findByInvoiceNumber(String invoiceNumber) {
        return factureJpaRepository.findByInvoiceNumber(invoiceNumber)
                .map(factureEntityMapper::toModel);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Facture> findByReservationId(Long idReservation) {
        return factureJpaRepository.findByReservation_IdReservation(idReservation)
                .map(factureEntityMapper::toModel);
    }

    @Override
    public void delete(Facture facture) {
        FactureEntity entity = factureEntityMapper.toEntity(facture);
        factureJpaRepository.delete(entity);
    }
}
