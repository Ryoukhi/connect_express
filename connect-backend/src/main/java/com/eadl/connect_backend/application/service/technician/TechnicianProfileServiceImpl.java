package com.eadl.connect_backend.application.service.technician;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.eadl.connect_backend.domain.model.reservation.ReservationStatus;
import com.eadl.connect_backend.domain.model.technician.AvailabilityStatus;
import com.eadl.connect_backend.domain.model.technician.TechnicianProfile;
import com.eadl.connect_backend.domain.port.exception.BusinessException;
import com.eadl.connect_backend.domain.port.in.technician.TechnicianProfileService;
import com.eadl.connect_backend.domain.port.out.persistence.ReservationRepository;
import com.eadl.connect_backend.domain.port.out.persistence.ReviewRepository;
import com.eadl.connect_backend.domain.port.out.persistence.TechnicianProfileRepository;
import com.eadl.connect_backend.domain.port.out.security.CurrentUserProvider;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TechnicianProfileServiceImpl implements TechnicianProfileService {

    private final TechnicianProfileRepository profileRepository;
    private final CurrentUserProvider currentUserProvider;
    private final ReservationRepository reservationRepository;
    private final ReviewRepository reviewRepository;

    public TechnicianProfileServiceImpl(TechnicianProfileRepository profileRepository,
                                        CurrentUserProvider currentUserProvider, ReservationRepository reservationRepository,
                                        ReviewRepository reviewRepository) {
        this.profileRepository = profileRepository;
        this.currentUserProvider = currentUserProvider;
        this.reservationRepository = reservationRepository;
        this.reviewRepository = reviewRepository;
    }

    // ================= CREATE =================
    @Override
    public TechnicianProfile createProfile(TechnicianProfile profile) {
        Long technicianId = currentUserProvider.getCurrentUserId();
        log.info("Création d'un profil pour le technicien id={}", technicianId);

        profileRepository.findByIdTechnician(technicianId)
                .ifPresent(p -> {
                    log.error("Profil existant déjà pour le technicien id={}", technicianId);
                    throw new IllegalStateException("Profile already exists for this technician");
                });

        profile.setIdTechnician(technicianId);
        profile.setVerified(false);
        profile.setCompletedJobs(0);
        profile.setAverageRating(BigDecimal.ZERO);

        TechnicianProfile saved = profileRepository.save(profile);
        log.info("Profil créé avec succès pour le technicien id={}", technicianId);
        return saved;
    }

    // ================= READ =================
    @Override
    public Optional<TechnicianProfile> getProfileByTechnicianId(Long technicianId) {
        log.debug("Récupération du profil du technicien id={}", technicianId);
        return profileRepository.findByTechnicianId(technicianId);
    }

    // ================= UPDATE PROFILE =================
    @Override
    public TechnicianProfile updateProfile(TechnicianProfile profile) {
        log.info("Mise à jour du profil du technicien id={}", profile.getIdTechnician());

        TechnicianProfile existing = profileRepository
                .findByTechnicianId(profile.getIdTechnician())
                .orElseThrow(() -> {
                    log.error("Profil introuvable pour le technicien id={}", profile.getIdTechnician());
                    return new BusinessException("Technician profile not found") {
                    };
                });

        existing.setBio(profile.getBio());
        existing.setIdCategory(profile.getIdCategory());
        existing.setYearsExperience(profile.getYearsExperience());
        existing.setHourlyRate(profile.getHourlyRate());
        existing.setVerified(false);

        TechnicianProfile saved = profileRepository.save(existing);
        log.info("Profil mis à jour pour le technicien id={}", profile.getIdTechnician());
        return saved;
    }

    // ================= AVAILABILITY =================
    @Override
    public TechnicianProfile updateAvailability(Long technicianId, AvailabilityStatus availabilityStatus) {
        log.info("Mise à jour de la disponibilité du technicien id={} en {}", technicianId, availabilityStatus);

        TechnicianProfile profile = profileRepository
                .findByTechnicianId(technicianId)
                .orElseThrow(() -> {
                    log.error("Profil introuvable pour le technicien id={}", technicianId);
                    return new BusinessException("Technician profile not found") {
                    };
                });

        profile.setAvailabilityStatus(availabilityStatus);
        TechnicianProfile saved = profileRepository.save(profile);
        log.debug("Disponibilité mise à jour pour le technicien id={}", technicianId);
        return saved;
    }

    // ================= HOURLY RATE =================
    @Override
    public TechnicianProfile updateHourlyRate(Long technicianId, BigDecimal hourlyRate) {
        log.info("Mise à jour du tarif horaire pour le technicien id={}", technicianId);

        if (hourlyRate == null || hourlyRate.compareTo(BigDecimal.ZERO) <= 0) {
            log.error("Tarif horaire invalide: {}", hourlyRate);
            throw new BusinessException("Hourly rate must be positive") {
            };
        }

        TechnicianProfile profile = profileRepository
                .findByTechnicianId(technicianId)
                .orElseThrow(() -> {
                    log.error("Profil introuvable pour le technicien id={}", technicianId);
                    return new BusinessException("Technician profile not found") {
                    };
                });

        profile.setHourlyRate(hourlyRate);
        TechnicianProfile saved = profileRepository.save(profile);
        log.debug("Tarif horaire mis à jour pour le technicien id={} à {}", technicianId, hourlyRate);
        return saved;
    }

    // ================= VALIDATION =================
    @Override
    public void validateProfile(Long technicianId) {
        log.info("Validation du profil du technicien id={}", technicianId);

        TechnicianProfile profile = profileRepository
                .findByTechnicianId(technicianId)
                .orElseThrow(() -> {
                    log.error("Profil introuvable pour validation id={}", technicianId);
                    return new BusinessException("Technician profile not found") {
                    };
                });

        profile.setVerified(true);
        profileRepository.save(profile);
        log.info("Profil validé pour le technicien id={}", technicianId);
    }

    @Override
    public void rejectProfile(Long technicianId, String reason) {
        log.info("Rejet du profil du technicien id={}, raison='{}'", technicianId, reason);

        TechnicianProfile profile = profileRepository
                .findByTechnicianId(technicianId)
                .orElseThrow(() -> {
                    log.error("Profil introuvable pour rejet id={}", technicianId);
                    return new BusinessException("Technician profile not found") {
                    };
                });

        profile.setVerified(false);
        profileRepository.save(profile);
        log.info("Profil rejeté pour le technicien id={}", technicianId);
    }

    // ================= ADMIN =================
    @Override
    public List<TechnicianProfile> getPendingProfiles() {
        log.debug("Récupération des profils non vérifiés");
        List<TechnicianProfile> pending = profileRepository.findByVerifiedFalse();
        log.debug("Nombre de profils en attente: {}", pending.size());
        return pending;
    }

    // ================= STATS =================
    @Override
    public void updateStatisticsAfterJob(Long technicianId, BigDecimal newRating) {
        log.info("Mise à jour des statistiques après job pour le technicien id={}, nouvelle note={}", technicianId, newRating);

        TechnicianProfile profile = profileRepository
                .findByTechnicianId(technicianId)
                .orElseThrow(() -> {
                    log.error("Profil introuvable pour mise à jour stats id={}", technicianId);
                    return new BusinessException("Technician profile not found") {
                    };
                });

        int completedJobs = profile.getCompletedJobs() + 1;
        profile.setCompletedJobs(completedJobs);

        BigDecimal totalRating = profile.getAverageRating()
                .multiply(BigDecimal.valueOf(completedJobs - 1))
                .add(newRating);

        profile.setAverageRating(
                totalRating.divide(BigDecimal.valueOf(completedJobs), 2, BigDecimal.ROUND_HALF_UP)
        );

        profileRepository.save(profile);
        log.debug("Stats mises à jour: completedJobs={}, averageRating={}", completedJobs, profile.getAverageRating());
    }

    @Override
    public void recalculateCompletedJobs(Long technicianId) {
        log.info("Recalcul du nombre de jobs terminés pour le technicien id={}", technicianId);

        TechnicianProfile profile = profileRepository.findByIdTechnician(technicianId)
                .orElseThrow(() -> {
                    log.error("Profil introuvable pour recalcul completedJobs id={}", technicianId);
                    return new IllegalStateException("Profile not found");
                });

        long completedJobs = reservationRepository.countByIdTechnicianAndStatus(
                technicianId,
                ReservationStatus.COMPLETED
        );

        profile.setCompletedJobs((int) completedJobs);
        profileRepository.save(profile);
        log.debug("Nombre de jobs terminés recalculé: {}", completedJobs);
    }

    @Override
    public void recalculateAverageRating(Long technicianId) {
        log.info("Recalcul de la note moyenne pour le technicien id={}", technicianId);

        TechnicianProfile profile = profileRepository.findByIdTechnician(technicianId)
                .orElseThrow(() -> {
                    log.error("Profil introuvable pour recalcul averageRating id={}", technicianId);
                    return new IllegalStateException("Profile not found");
                });

        List<Long> reviewIds =
                reservationRepository.findReviewIdsByIdTechnicianAndStatus(
                        technicianId,
                        ReservationStatus.COMPLETED
                );

        if (reviewIds.isEmpty()) {
            profile.setAverageRating(BigDecimal.ZERO);
            profileRepository.save(profile);
            log.debug("Aucune review trouvée, note moyenne mise");
        }
    }
}
