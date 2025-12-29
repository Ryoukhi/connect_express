package com.eadl.connect_backend.application.service.technician;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.eadl.connect_backend.domain.model.reservation.ReservationStatus;
import com.eadl.connect_backend.domain.model.technician.AvailabilityStatus;
import com.eadl.connect_backend.domain.model.technician.TechnicianProfile;
import com.eadl.connect_backend.domain.port.exception.BusinessException;
import com.eadl.connect_backend.domain.port.in.technician.TechnicianProfileService;
import com.eadl.connect_backend.domain.port.out.persistence.ReservationRepository;
import com.eadl.connect_backend.domain.port.out.persistence.ReviewRepository;
import com.eadl.connect_backend.domain.port.out.persistence.TechnicianProfileRepository;
import com.eadl.connect_backend.domain.port.out.security.CurrentUserProvider;

public class TechnicianProfileServiceImpl implements TechnicianProfileService {

    private final TechnicianProfileRepository profileRepository;
    private final CurrentUserProvider currentUserProvider;
    private final ReservationRepository reservationRepository;
    private final ReviewRepository reviewRepository;

    

    // ================= CREATE =================

    public TechnicianProfileServiceImpl(TechnicianProfileRepository profileRepository,
            CurrentUserProvider currentUserProvider, ReservationRepository reservationRepository,
            ReviewRepository reviewRepository) {
        this.profileRepository = profileRepository;
        this.currentUserProvider = currentUserProvider;
        this.reservationRepository = reservationRepository;
        this.reviewRepository = reviewRepository;
    }


    @Override
public TechnicianProfile createProfile(TechnicianProfile profile) {

    Long technicianId = currentUserProvider.getCurrentUserId();

    // Vérifier qu’un profil n’existe pas déjà
    profileRepository.findByIdTechnician(technicianId)
            .ifPresent(p -> {
                throw new IllegalStateException("Profile already exists for this technician");
            });

    profile.setIdTechnician(technicianId);
    profile.setVerified(false);
    profile.setCompletedJobs(0);
    profile.setAverageRating(BigDecimal.ZERO);

    return profileRepository.save(profile);
}


    // ================= READ =================

    @Override
    public Optional<TechnicianProfile> getProfileByTechnicianId(Long technicianId) {
        return profileRepository.findByTechnicianId(technicianId);
    }

    // ================= UPDATE PROFILE =================

    @Override
    public TechnicianProfile updateProfile(TechnicianProfile profile) {

        TechnicianProfile existing = profileRepository
                .findByTechnicianId(profile.getIdTechnician())
                .orElseThrow(() -> new BusinessException("Technician profile not found") {});

        existing.setBio(profile.getBio());
        existing.setIdCategory(profile.getIdCategory());
        existing.setYearsExperience(profile.getYearsExperience());
        existing.setHourlyRate(profile.getHourlyRate());
        existing.setProfilePhotoUrl(profile.getProfilePhotoUrl());

        // Toute modification invalide la validation
        existing.setVerified(false);

        return profileRepository.save(existing);
    }

    // ================= AVAILABILITY =================

    @Override
    public TechnicianProfile updateAvailability(Long technicianId, AvailabilityStatus availabilityStatus) {

        TechnicianProfile profile = profileRepository
                .findByTechnicianId(technicianId)
                .orElseThrow(() -> new BusinessException("Technician profile not found") {});

        profile.setAvailabilityStatus(availabilityStatus);

        return profileRepository.save(profile);
    }

    // ================= HOURLY RATE =================

    @Override
    public TechnicianProfile updateHourlyRate(Long technicianId, BigDecimal hourlyRate) {

        if (hourlyRate == null || hourlyRate.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Hourly rate must be positive") {};
        }

        TechnicianProfile profile = profileRepository
                .findByTechnicianId(technicianId)
                .orElseThrow(() -> new BusinessException("Technician profile not found") {});

        profile.setHourlyRate(hourlyRate);

        return profileRepository.save(profile);
    }

    // ================= VALIDATION =================

    @Override
    public void validateProfile(Long technicianId) {

        TechnicianProfile profile = profileRepository
                .findByTechnicianId(technicianId)
                .orElseThrow(() -> new BusinessException("Technician profile not found") {});

        profile.setVerified(true);

        profileRepository.save(profile);
    }

    @Override
    public void rejectProfile(Long technicianId, String reason) {

        TechnicianProfile profile = profileRepository
                .findByTechnicianId(technicianId)
                .orElseThrow(() -> new BusinessException("Technician profile not found") {});

        profile.setVerified(false);

        // Optionnel : log admin / notification
        profileRepository.save(profile);
    }

    // ================= ADMIN =================

    @Override
    public List<TechnicianProfile> getPendingProfiles() {
        return profileRepository.findByVerifiedFalse();
    }

    // ================= STATS =================

    @Override
    public void updateStatisticsAfterJob(Long technicianId, BigDecimal newRating) {

        TechnicianProfile profile = profileRepository
                .findByTechnicianId(technicianId)
                .orElseThrow(() -> new BusinessException("Technician profile not found") {});

        int completedJobs = profile.getCompletedJobs() + 1;
        profile.setCompletedJobs(completedJobs);

        BigDecimal totalRating = profile.getAverageRating()
                .multiply(BigDecimal.valueOf(completedJobs - 1))
                .add(newRating);

        profile.setAverageRating(
                totalRating.divide(BigDecimal.valueOf(completedJobs), 2, BigDecimal.ROUND_HALF_UP)
        );

        profileRepository.save(profile);
    }

    @Override
    public void recalculateCompletedJobs(Long technicianId) {

        TechnicianProfile profile = profileRepository.findByIdTechnician(technicianId)
                .orElseThrow(() -> new IllegalStateException("Profile not found"));

        long completedJobs = reservationRepository.countByIdTechnicianAndStatus(
                technicianId,
                ReservationStatus.COMPLETED
        );

        profile.setCompletedJobs((int) completedJobs);
        profileRepository.save(profile);
    }


    @Override
    public void recalculateAverageRating(Long technicianId) {

        TechnicianProfile profile = profileRepository.findByIdTechnician(technicianId)
                .orElseThrow(() -> new IllegalStateException("Profile not found"));

        List<Long> reviewIds =
                reservationRepository.findReviewIdsByIdTechnicianAndStatus(
                        technicianId,
                        ReservationStatus.COMPLETED
                );

        if (reviewIds.isEmpty()) {
            profile.setAverageRating(BigDecimal.ZERO);
            profileRepository.save(profile);
            return;
        }

        BigDecimal averageRating =
                reviewRepository.calculateAverageRating(reviewIds);

        profile.setAverageRating(
                averageRating != null ? averageRating : BigDecimal.ZERO
        );

        profileRepository.save(profile);
    }

}