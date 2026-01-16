package com.eadl.connect_backend.application.service.technician;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.eadl.connect_backend.application.dto.TechnicianResultSearchDto;
import com.eadl.connect_backend.domain.model.technician.AvailabilityStatus;
import com.eadl.connect_backend.domain.model.technician.TechnicianSkill;
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
    private final com.eadl.connect_backend.domain.port.out.persistence.TechnicianSkillRepository technicianSkillRepository;
    private final com.eadl.connect_backend.domain.port.out.persistence.CategoryRepository categoryRepository;
    private final com.eadl.connect_backend.domain.port.out.persistence.ReservationRepository reservationRepository;

    public TechnicianServiceImpl(TechnicianRepository technicianRepository,
                                 com.eadl.connect_backend.domain.port.out.persistence.TechnicianSkillRepository technicianSkillRepository,
                                 com.eadl.connect_backend.domain.port.out.persistence.CategoryRepository categoryRepository,
                                 com.eadl.connect_backend.domain.port.out.persistence.ReservationRepository reservationRepository) {
        this.technicianRepository = technicianRepository;
        this.technicianSkillRepository = technicianSkillRepository;
        this.categoryRepository = categoryRepository;
        this.reservationRepository = reservationRepository;
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

    @Override
    public List<TechnicianResultSearchDto> searchTechnicians(String city, String neighborhood, String categoryName, AvailabilityStatus availabilityStatus, Double minRating, Double minPrice, Double maxPrice) {
        log.info("Recherche de techniciens avec critères: ville={}, quartier={}, catégorie={}, disponibilité={}, minRating={}, minPrice={}, maxPrice={}", city, neighborhood, categoryName, availabilityStatus, minRating, minPrice, maxPrice);

        // Récupération des compétences correspondant aux critères de catégorie
        List<TechnicianSkill> skills;
        if (categoryName != null) {
            skills = categoryRepository.findByName(categoryName)
                    .map(cat -> technicianSkillRepository.findByCategoryId(cat.getIdCategory()))
                    .orElse(Collections.emptyList());
        } else {
            skills = technicianSkillRepository.findAll();
        }

        List<TechnicianResultSearchDto> results = skills.stream()
                .filter(skill -> {
                    // Disponibilité
                    if (availabilityStatus != null && skill.getAvailabilityStatus() != availabilityStatus) return false;

                    // Tarif horaire
                    if (minPrice != null && (skill.getHourlyRate() == null || skill.getHourlyRate().doubleValue() < minPrice)) return false;
                    if (maxPrice != null && (skill.getHourlyRate() == null || skill.getHourlyRate().doubleValue() > maxPrice)) return false;

                    return true;
                })
                .map(skill -> {
                    // Récupérer le technicien lié
                    return technicianRepository.findById(skill.getIdUser())
                            .map(tech -> new java.util.AbstractMap.SimpleEntry<>(skill, tech))
                            .orElse(null);
                })
                .filter(entry -> entry != null)
                .map(entry -> {
                    com.eadl.connect_backend.domain.model.technician.TechnicianSkill skill = entry.getKey();
                    com.eadl.connect_backend.domain.model.user.Technician tech = entry.getValue();

                    // Filtres sur ville / quartier
                    if (city != null && (tech.getCity() == null || !city.equalsIgnoreCase(tech.getCity()))) return null;
                    if (neighborhood != null && (tech.getNeighborhood() == null || !neighborhood.equalsIgnoreCase(tech.getNeighborhood()))) return null;

                    // Note minimale
                    Double averageRating = reservationRepository.averageRatingByTechnicianIdAndStatus(tech.getIdUser(), com.eadl.connect_backend.domain.model.reservation.ReservationStatus.COMPLETED);
                    if (averageRating == null) averageRating = 0.0;
                    if (minRating != null && averageRating < minRating) return null;

                    return new TechnicianResultSearchDto(
                            tech.getIdUser(),
                            tech.getFullName(),
                            skill.isVerified(),
                            averageRating,
                            skill.getAvailabilityStatus(),
                            skill.getHourlyRate() != null ? skill.getHourlyRate().doubleValue() : 0.0,
                            skill.getName(),
                            skill.getYearsExperience() != null ? skill.getYearsExperience() : 0,
                            tech.getCity(),
                            tech.getNeighborhood()
                    );
                })
                .filter(dto -> dto != null)
                .collect(Collectors.toList());

        log.debug("Nombre de techniciens trouvés: {}", results.size());
        return results;
    }

    @Override
    public void updateAvailabilityStatus(Long technicianId, AvailabilityStatus status) {
            log.info("Mise à jour du statut de disponibilité pour le technicien id={} vers {}", technicianId, status);
        
            // Récupérer la compétence(s) du technicien via le repository (port)
            List<TechnicianSkill> skills = technicianSkillRepository.findByUserId(technicianId);
        
            // Mettre à jour le statut si une compétence existe
            if (!skills.isEmpty()) {
                TechnicianSkill skill = skills.get(0);
                skill.setAvailabilityStatus(status);
                technicianSkillRepository.save(skill);
                log.debug("Statut de disponibilité mis à jour avec succès pour le technicien id={}", technicianId);
            } else {
                log.warn("Aucune compétence trouvée pour le technicien id={}", technicianId);
            }
        
            log.debug("Statut de disponibilité mis à jour pour {} compétences du technicien", skills.size());
    }
}
