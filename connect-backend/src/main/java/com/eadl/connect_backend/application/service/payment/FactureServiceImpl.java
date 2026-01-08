package com.eadl.connect_backend.application.service.payment;

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
import com.eadl.connect_backend.domain.port.in.payment.FactureService;
import com.eadl.connect_backend.domain.port.out.persistence.FactureRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Implémentation du service Facture
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class FactureServiceImpl implements FactureService {

    private final FactureRepository factureRepository; // port sortant
    private final FactureMapper factureMapper;

    @Override
    public FactureDto createFacture(Facture facture) {
        log.info("Création d'une nouvelle facture");

        if (facture.getInvoiceNumber() == null || facture.getInvoiceNumber().isBlank()) {
            String generatedNumber = generateInvoiceNumber();
            facture.setInvoiceNumber(generatedNumber);
            log.debug("Invoice number généré: {}", generatedNumber);
        }

        facture.setGeneratedAt(LocalDateTime.now());
        log.debug("Facture générée à {}", facture.getGeneratedAt());

        Facture savedFacture = factureRepository.save(facture);
        log.info("Facture créée avec succès id={}, invoiceNumber={}", savedFacture.getId(), savedFacture.getInvoiceNumber());

        return factureMapper.toDto(savedFacture);
    }

    @Override
    public FactureDto updateFacture(Long idFacture, Facture facture) {
        log.info("Mise à jour de la facture id={}", idFacture);

        Facture existing = factureRepository.findById(idFacture)
                .orElseThrow(() -> {
                    log.error("Facture non trouvée pour id={}", idFacture);
                    return new IllegalArgumentException("Facture non trouvée : " + idFacture);
                });

        existing.setAmount(facture.getAmount());
        existing.setPdfUrl(facture.getPdfUrl());
        existing.setGeneratedAt(facture.getGeneratedAt());
        existing.setInvoiceNumber(facture.getInvoiceNumber());

        Facture updatedFacture = factureRepository.save(existing);
        log.info("Facture mise à jour id={}, invoiceNumber={}", updatedFacture.getId(), updatedFacture.getInvoiceNumber());

        return factureMapper.toDto(updatedFacture);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FactureDto> getFactureById(Long idFacture) {
        log.debug("Récupération de la facture id={}", idFacture);
        return factureRepository.findById(idFacture)
                .map(factureMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FactureDto> getAllFactures() {
        log.debug("Récupération de toutes les factures");
        List<FactureDto> factures = factureRepository.findAll()
                .stream()
                .map(factureMapper::toDto)
                .collect(Collectors.toList());
        log.debug("Nombre de factures récupérées: {}", factures.size());
        return factures;
    }

    @Override
    public void deleteFacture(Long idFacture) {
        log.info("Suppression de la facture id={}", idFacture);
        factureRepository.deleteById(idFacture);
        log.info("Facture id={} supprimée", idFacture);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FactureDto> getFacturesByReservationId(Long idReservation) {
        log.debug("Récupération des factures pour la réservation id={}", idReservation);
        List<FactureDto> factures = factureRepository.findByReservationId(idReservation)
                .stream()
                .map(factureMapper::toDto)
                .collect(Collectors.toList());
        log.debug("Nombre de factures récupérées pour la réservation id={}: {}", idReservation, factures.size());
        return factures;
    }

    @Override
    public String generateInvoiceNumber() {
        String invoiceNumber = "INV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        log.debug("Numéro de facture généré: {}", invoiceNumber);
        return invoiceNumber;
    }

    @Override
    public String generatePdf(Facture facture) {
        String pdfUrl = "https://example.com/factures/" + facture.getInvoiceNumber() + ".pdf";
        log.debug("URL PDF générée pour la facture id={} : {}", facture.getId(), pdfUrl);
        return pdfUrl;
    }
}
