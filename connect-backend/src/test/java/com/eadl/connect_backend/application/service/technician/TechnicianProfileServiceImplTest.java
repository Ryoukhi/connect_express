package com.eadl.connect_backend.application.service.technician;

import com.eadl.connect_backend.domain.model.reservation.ReservationStatus;
import com.eadl.connect_backend.domain.model.technician.AvailabilityStatus;
import com.eadl.connect_backend.domain.model.technician.TechnicianProfile;
import com.eadl.connect_backend.domain.port.exception.BusinessException;
import com.eadl.connect_backend.domain.port.out.persistence.ReservationRepository;
import com.eadl.connect_backend.domain.port.out.persistence.ReviewRepository;
import com.eadl.connect_backend.domain.port.out.persistence.TechnicianProfileRepository;
import com.eadl.connect_backend.domain.port.out.security.CurrentUserProvider;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TechnicianProfileServiceImplTest {

    @Mock
    private TechnicianProfileRepository profileRepository;

    @Mock
    private CurrentUserProvider currentUserProvider;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private TechnicianProfileServiceImpl service;

    private TechnicianProfile profile;

    @BeforeEach
    void setUp() {
        profile = new TechnicianProfile();
        profile.setIdTechnician(1L);
        profile.setVerified(false);
        profile.setCompletedJobs(0);
        profile.setAverageRating(BigDecimal.ZERO);
        profile.setHourlyRate(BigDecimal.valueOf(20));
    }

    // ================= CREATE =================

    @Test
    void shouldCreateProfileSuccessfully() {
        when(currentUserProvider.getCurrentUserId()).thenReturn(1L);
        when(profileRepository.findByIdTechnician(1L)).thenReturn(Optional.empty());
        when(profileRepository.save(any())).thenReturn(profile);

        TechnicianProfile result = service.createProfile(profile);

        assertThat(result.getIdTechnician()).isEqualTo(1L);
        assertThat(result.isVerified()).isFalse();
        assertThat(result.getCompletedJobs()).isZero();
        assertThat(result.getAverageRating()).isEqualTo(BigDecimal.ZERO);
    }

    @Test
    void shouldThrowIfProfileAlreadyExists() {
        when(currentUserProvider.getCurrentUserId()).thenReturn(1L);
        when(profileRepository.findByIdTechnician(1L))
                .thenReturn(Optional.of(profile));

        assertThatThrownBy(() ->
                service.createProfile(profile))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Profile already exists");
    }

    // ================= READ =================

    @Test
    void shouldReturnProfileByTechnicianId() {
        when(profileRepository.findByTechnicianId(1L))
                .thenReturn(Optional.of(profile));

        Optional<TechnicianProfile> result =
                service.getProfileByTechnicianId(1L);

        assertThat(result).isPresent();
    }

    // ================= UPDATE PROFILE =================

    @Test
    void shouldUpdateProfileSuccessfully() {
        when(profileRepository.findByTechnicianId(1L))
                .thenReturn(Optional.of(profile));
        when(profileRepository.save(any())).thenReturn(profile);

        TechnicianProfile updated = new TechnicianProfile();
        updated.setIdTechnician(1L);
        updated.setBio("New bio");
        updated.setIdCategory(2L);
        updated.setYearsExperience(5);
        updated.setHourlyRate(BigDecimal.valueOf(30));

        TechnicianProfile result = service.updateProfile(updated);

        assertThat(result.getBio()).isEqualTo("New bio");
        assertThat(result.getHourlyRate()).isEqualTo(BigDecimal.valueOf(30));
        assertThat(result.isVerified()).isFalse();
    }

    @Test
    void shouldThrowIfProfileNotFoundOnUpdate() {
        when(profileRepository.findByTechnicianId(1L))
                .thenReturn(Optional.empty());

        TechnicianProfile updated = new TechnicianProfile();
        updated.setIdTechnician(1L);

        assertThatThrownBy(() ->
                service.updateProfile(updated))
                .isInstanceOf(BusinessException.class);
    }

    // ================= AVAILABILITY =================

    @Test
    void shouldUpdateAvailability() {
        when(profileRepository.findByTechnicianId(1L))
                .thenReturn(Optional.of(profile));
        when(profileRepository.save(any())).thenReturn(profile);

        TechnicianProfile result =
                service.updateAvailability(1L, AvailabilityStatus.AVAILABLE);

        assertThat(result.getAvailabilityStatus())
                .isEqualTo(AvailabilityStatus.AVAILABLE);
    }

    // ================= HOURLY RATE =================

    @Test
    void shouldUpdateHourlyRateSuccessfully() {
        when(profileRepository.findByTechnicianId(1L))
                .thenReturn(Optional.of(profile));
        when(profileRepository.save(any())).thenReturn(profile);

        TechnicianProfile result =
                service.updateHourlyRate(1L, BigDecimal.valueOf(50));

        assertThat(result.getHourlyRate()).isEqualTo(BigDecimal.valueOf(50));
    }

    @Test
    void shouldThrowIfHourlyRateIsInvalid() {
        assertThatThrownBy(() ->
                service.updateHourlyRate(1L, BigDecimal.ZERO))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Hourly rate must be positive");
    }

    // ================= VALIDATION =================

    @Test
    void shouldValidateProfile() {
        when(profileRepository.findByTechnicianId(1L))
                .thenReturn(Optional.of(profile));

        service.validateProfile(1L);

        assertThat(profile.isVerified()).isTrue();
        verify(profileRepository).save(profile);
    }

    @Test
    void shouldRejectProfile() {
        when(profileRepository.findByTechnicianId(1L))
                .thenReturn(Optional.of(profile));

        service.rejectProfile(1L, "Incomplete");

        assertThat(profile.isVerified()).isFalse();
        verify(profileRepository).save(profile);
    }

    // ================= ADMIN =================

    @Test
    void shouldReturnPendingProfiles() {
        when(profileRepository.findByVerifiedFalse())
                .thenReturn(List.of(profile));

        List<TechnicianProfile> result =
                service.getPendingProfiles();

        assertThat(result).hasSize(1);
    }

    // ================= STATS =================

    @Test
    void shouldUpdateStatisticsAfterJob() {
        when(profileRepository.findByTechnicianId(1L))
                .thenReturn(Optional.of(profile));

        service.updateStatisticsAfterJob(1L, BigDecimal.valueOf(4));

        assertThat(profile.getCompletedJobs()).isEqualTo(1);
        assertThat(profile.getAverageRating())
                .isEqualTo(BigDecimal.valueOf(4.00).setScale(2));
    }

    @Test
    void shouldRecalculateCompletedJobs() {
        when(profileRepository.findByIdTechnician(1L))
                .thenReturn(Optional.of(profile));
        when(reservationRepository.countByIdTechnicianAndStatus(
                1L, ReservationStatus.COMPLETED))
                .thenReturn(3L);

        service.recalculateCompletedJobs(1L);

        assertThat(profile.getCompletedJobs()).isEqualTo(3);
    }

    @Test
    void shouldRecalculateAverageRatingWithReviews() {
        when(profileRepository.findByIdTechnician(1L))
                .thenReturn(Optional.of(profile));
        when(reservationRepository.findReviewIdsByIdTechnicianAndStatus(
                1L, ReservationStatus.COMPLETED))
                .thenReturn(List.of(10L, 11L));
        when(reviewRepository.calculateAverageRating(List.of(10L, 11L)))
                .thenReturn(BigDecimal.valueOf(4.5));

        service.recalculateAverageRating(1L);

        assertThat(profile.getAverageRating()).isEqualTo(BigDecimal.valueOf(4.5));
    }

    @Test
    void shouldSetAverageRatingToZeroIfNoReviews() {
        when(profileRepository.findByIdTechnician(1L))
                .thenReturn(Optional.of(profile));
        when(reservationRepository.findReviewIdsByIdTechnicianAndStatus(
                1L, ReservationStatus.COMPLETED))
                .thenReturn(List.of());

        service.recalculateAverageRating(1L);

        assertThat(profile.getAverageRating()).isEqualTo(BigDecimal.ZERO);
    }
}

