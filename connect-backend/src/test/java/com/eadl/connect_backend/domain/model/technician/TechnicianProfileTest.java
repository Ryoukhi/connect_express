package com.eadl.connect_backend.domain.model.technician;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TechnicianProfileTest {

    @Test
    @DisplayName("create() doit initialiser correctement un profil technicien")
    void create_shouldInitializeProfileCorrectly() {
        // given
        Long technicianId = 1L;
        String bio = "Technicien électricien";
        Integer experience = 5;
        BigDecimal hourlyRate = BigDecimal.valueOf(5000);

        // when
        TechnicianProfile profile =
                TechnicianProfile.create(technicianId, bio, experience, hourlyRate);

        // then
        assertNotNull(profile);
        assertEquals(technicianId, profile.getIdTechnician());
        assertEquals(bio, profile.getBio());
        assertEquals(experience, profile.getYearsExperience());
        assertEquals(hourlyRate, profile.getHourlyRate());
        assertFalse(profile.getVerified());
        assertEquals(AvailabilityStatus.UNAVAILABLE, profile.getAvailabilityStatus());
        assertEquals(0, profile.getCompletedJobs());
        assertEquals(BigDecimal.ZERO, profile.getAverageRating());
    }

    @Test
    @DisplayName("updateProfile() doit mettre à jour les informations du profil")
    void updateProfile_shouldUpdateFields() {
        // given
        TechnicianProfile profile =
                TechnicianProfile.create(1L, "Ancien bio", 2, BigDecimal.valueOf(3000));

        // when
        profile.updateProfile("Nouveau bio", 4, BigDecimal.valueOf(4500));

        // then
        assertEquals("Nouveau bio", profile.getBio());
        assertEquals(4, profile.getYearsExperience());
        assertEquals(BigDecimal.valueOf(4500), profile.getHourlyRate());
    }

    @Test
    @DisplayName("verify() doit marquer le profil comme vérifié")
    void verify_shouldMarkProfileAsVerified() {
        // given
        TechnicianProfile profile =
                TechnicianProfile.create(1L, "Bio", 3, BigDecimal.valueOf(4000));

        // when
        profile.verify();

        // then
        assertTrue(profile.isVerified());
    }

    @Test
    @DisplayName("Changement de disponibilité du technicien")
    void availabilityStatus_shouldChangeCorrectly() {
        // given
        TechnicianProfile profile =
                TechnicianProfile.create(1L, "Bio", 3, BigDecimal.valueOf(4000));

        // when / then
        profile.setAvailable();
        assertEquals(AvailabilityStatus.AVAILABLE, profile.getAvailabilityStatus());
        assertTrue(profile.isAvailable());

        profile.setBusy();
        assertEquals(AvailabilityStatus.BUSY, profile.getAvailabilityStatus());

        profile.setOnBreak();
        assertEquals(AvailabilityStatus.ON_BREAK, profile.getAvailabilityStatus());

        profile.setUnavailable();
        assertEquals(AvailabilityStatus.UNAVAILABLE, profile.getAvailabilityStatus());
    }

    @Test
    @DisplayName("incrementCompletedJobs() doit incrémenter le nombre de missions")
    void incrementCompletedJobs_shouldIncreaseCounter() {
        // given
        TechnicianProfile profile =
                TechnicianProfile.create(1L, "Bio", 3, BigDecimal.valueOf(4000));

        // when
        profile.incrementCompletedJobs();
        profile.incrementCompletedJobs();

        // then
        assertEquals(2, profile.getCompletedJobs());
    }

    @Test
    @DisplayName("updateAverageRating() doit mettre à jour la note moyenne")
    void updateAverageRating_shouldUpdateRating() {
        // given
        TechnicianProfile profile =
                TechnicianProfile.create(1L, "Bio", 3, BigDecimal.valueOf(4000));

        BigDecimal rating = BigDecimal.valueOf(4.5);

        // when
        profile.updateAverageRating(rating);

        // then
        assertEquals(rating, profile.getAverageRating());
    }

    @Test
    @DisplayName("Deux profils avec le même id doivent être égaux")
    void equals_shouldBeBasedOnId() {
        // given
        TechnicianProfile p1 = new TechnicianProfile();
        p1.setIdProfile(10L);

        TechnicianProfile p2 = new TechnicianProfile();
        p2.setIdProfile(10L);

        // then
        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    @DisplayName("Deux profils avec des ids différents ne sont pas égaux")
    void equals_shouldReturnFalseForDifferentIds() {
        // given
        TechnicianProfile p1 = new TechnicianProfile();
        p1.setIdProfile(1L);

        TechnicianProfile p2 = new TechnicianProfile();
        p2.setIdProfile(2L);

        // then
        assertNotEquals(p1, p2);
    }
}
