package com.eadl.connect_backend.application.service.payment;


import org.springframework.stereotype.Service;

import com.eadl.connect_backend.domain.model.payment.Facture;

import com.eadl.connect_backend.domain.port.exception.FactureNotFoundException;
import com.eadl.connect_backend.domain.port.in.payment.FactureService;
import com.eadl.connect_backend.domain.port.out.external.StorageService;
import com.eadl.connect_backend.domain.port.out.persistence.FactureRepository;
import jakarta.transaction.Transactional;

/**
 * Implémentation du service Facture
 */
@Service
@Transactional
public class FactureServiceImpl implements FactureService {
    
    private final FactureRepository factureRepository;

    private final StorageService storageService;

    public FactureServiceImpl(FactureRepository factureRepository, StorageService storageService) {
        this.factureRepository = factureRepository;
        this.storageService = storageService;
    }


    @Override
    public byte[] downloadFacturePdf(Long idFacture) {
        Facture facture = factureRepository.findById(idFacture)
            .orElseThrow(() -> new FactureNotFoundException("Facture non trouvée"));
        
        return storageService.downloadFile(facture.getPdfUrl());
    }
    
    
}