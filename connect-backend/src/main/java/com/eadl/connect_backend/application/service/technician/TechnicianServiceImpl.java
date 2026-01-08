package com.eadl.connect_backend.application.service.technician;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.eadl.connect_backend.domain.model.user.Technician;
import com.eadl.connect_backend.domain.port.in.technician.TechnicianService;
import com.eadl.connect_backend.domain.port.out.persistence.TechnicianRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class TechnicianServiceImpl implements TechnicianService {

    private final TechnicianRepository technicianRepository;

    public TechnicianServiceImpl(TechnicianRepository technicianRepository) {
        this.technicianRepository = technicianRepository;
    }

    @Override
    public Technician registerTechnician(Technician technician) {
        log.info("Enregistrement d'un nouveau technicien: {}", technician.getEmail());
        Technician saved = technicianRepository.save(technician);
        log.debug("Technicien enregistré avec id={}", saved.getIdUser());
        return saved;
    }

    @Override
    public void validateKyc(Long technicianId) {
        log.info("Validation KYC pour le technicien id={}", technicianId);
        Optional<Technician> optTech = technicianRepository.findById(technicianId);

        if (optTech.isPresent()) {
            Technician tech = optTech.get();
            tech.activate();
            technicianRepository.save(tech);
            log.info("Technicien KYC validé id={}", technicianId);
        } else {
            log.error("Technicien introuvable pour KYC id={}", technicianId);
            throw new IllegalArgumentException("Technician not found with id " + technicianId);
        }
    }

    @Override
    public List<Technician> getActiveTechnicians() {
        log.debug("Récupération des techniciens actifs");
        List<Technician> activeTechs = technicianRepository.findAll().stream()
                .filter(Technician::isActive)
                .collect(Collectors.toList());
        log.debug("Nombre de techniciens actifs: {}", activeTechs.size());
        return activeTechs;
    }

    @Override
    public Long countActiveTechnicians() {
        long count = technicianRepository.findAll().stream()
                .filter(Technician::isActive)
                .count();
        log.debug("Nombre total de techniciens actifs: {}", count);
        return count;
    }

    @Override
    public List<Technician> getTechniciansByCity(String city) {
        log.info("Récupération des techniciens par ville: {}", city);
        List<Technician> results = technicianRepository.findAll().stream()
                .filter(tech -> tech.getCity() != null && city.equalsIgnoreCase(tech.getCity()))
                .collect(Collectors.toList());
        log.debug("Nombre de techniciens trouvés dans {}: {}", city, results.size());
        return results;
    }

    @Override
    public List<Technician> getTechniciansByNeighborhood(String neighborhood) {
        log.info("Récupération des techniciens par quartier: {}", neighborhood);
        List<Technician> results = technicianRepository.findAll().stream()
                .filter(tech -> tech.getNeighborhood() != null && neighborhood.equalsIgnoreCase(tech.getNeighborhood()))
                .collect(Collectors.toList());
        log.debug("Nombre de techniciens trouvés dans le quartier {}: {}", neighborhood, results.size());
        return results;
    }

    @Override
    public Optional<Technician> getTechnicianById(Long technicianId) {
        log.debug("Récupération du technicien par id={}", technicianId);
        return technicianRepository.findById(technicianId);
    }
}
