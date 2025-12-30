package com.eadl.connect_backend.application.service.payment;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eadl.connect_backend.application.dto.FactureDto;
import com.eadl.connect_backend.application.mapper.FactureMapper;
import com.eadl.connect_backend.domain.model.payment.Facture;

import com.eadl.connect_backend.domain.port.exception.FactureNotFoundException;
import com.eadl.connect_backend.domain.port.in.payment.FactureService;
import com.eadl.connect_backend.domain.port.out.external.StorageService;
import com.eadl.connect_backend.domain.port.out.persistence.FactureRepository;
import lombok.RequiredArgsConstructor;

/**
 * Implémentation du service Facture
 */
@Service
@RequiredArgsConstructor
@Transactional
public class FactureServiceImpl implements FactureService {

    private final FactureRepository factureRepository; // port sortant
    private final FactureMapper factureMapper;

    @Override
    public FactureDto createFacture(Facture facture) {
        if (facture.getInvoiceNumber() == null || facture.getInvoiceNumber().isBlank()) {
            facture.setInvoiceNumber(generateInvoiceNumber());
        }
        facture.setGeneratedAt(LocalDateTime.now());

        var savedFacture = factureRepository.save(facture); // passage par le port
        return factureMapper.toDto(savedFacture);
    }

    @Override
    public FactureDto updateFacture(Long idFacture, Facture facture) {
        Facture existing = factureRepository.findById(idFacture)
                .orElseThrow(() -> new IllegalArgumentException("Facture non trouvée : " + idFacture));

        existing.setAmount(facture.getAmount());
        existing.setPdfUrl(facture.getPdfUrl());
        existing.setGeneratedAt(facture.getGeneratedAt());
        existing.setInvoiceNumber(facture.getInvoiceNumber());

        var updatedFacture = factureRepository.save(existing);
        return factureMapper.toDto(updatedFacture);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FactureDto> getFactureById(Long idFacture) {
        return factureRepository.findById(idFacture)
                .map(factureMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FactureDto> getAllFactures() {
        return factureRepository.findAll()
                .stream()
                .map(factureMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteFacture(Long idFacture) {
        factureRepository.deleteById(idFacture);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FactureDto> getFacturesByReservationId(Long idReservation) {
        return factureRepository.findByReservationId(idReservation)
                .stream()
                .map(factureMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public String generateInvoiceNumber() {
        return "INV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    @Override
    public String generatePdf(Facture facture) {
        return "https://example.com/factures/" + facture.getInvoiceNumber() + ".pdf";
    }
}