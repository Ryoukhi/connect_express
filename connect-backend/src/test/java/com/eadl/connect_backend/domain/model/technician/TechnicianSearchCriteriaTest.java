package com.eadl.connect_backend.domain.model.technician;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TechnicianSearchCriteriaTest {

    @Test
    @DisplayName("Les critères doivent être null par défaut")
    void defaultValues_shouldBeNull() {
        // given
        TechnicianSearchCriteria criteria = new TechnicianSearchCriteria();

        // then
        assertNull(criteria.getCity());
        assertNull(criteria.getActiveOnly());
        assertNull(criteria.getVerifiedOnly());
        assertNull(criteria.getAvailabilityStatus());
        assertNull(criteria.getMinHourlyRate());
        assertNull(criteria.getMaxHourlyRate());
        assertNull(criteria.getCategoryId());
    }

    @Test
    @DisplayName("Les setters doivent correctement affecter les valeurs")
    void setters_shouldSetValuesCorrectly() {
        // given
        TechnicianSearchCriteria criteria = new TechnicianSearchCriteria();

        // when
        criteria.setCity("Douala");
        criteria.setActiveOnly(true);
        criteria.setVerifiedOnly(true);
        criteria.setAvailabilityStatus(AvailabilityStatus.AVAILABLE);
        criteria.setMinHourlyRate(BigDecimal.valueOf(3000));
        criteria.setMaxHourlyRate(BigDecimal.valueOf(8000));
        criteria.setCategoryId(5L);

        // then
        assertEquals("Douala", criteria.getCity());
        assertTrue(criteria.getActiveOnly());
        assertTrue(criteria.getVerifiedOnly());
        assertEquals(AvailabilityStatus.AVAILABLE, criteria.getAvailabilityStatus());
        assertEquals(BigDecimal.valueOf(3000), criteria.getMinHourlyRate());
        assertEquals(BigDecimal.valueOf(8000), criteria.getMaxHourlyRate());
        assertEquals(5L, criteria.getCategoryId());
    }

    @Test
    @DisplayName("Les critères peuvent être utilisés partiellement")
    void criteria_canBePartiallyFilled() {
        // given
        TechnicianSearchCriteria criteria = new TechnicianSearchCriteria();

        // when
        criteria.setVerifiedOnly(true);
        criteria.setAvailabilityStatus(AvailabilityStatus.BUSY);

        // then
        assertNull(criteria.getCity());
        assertNull(criteria.getActiveOnly());
        assertTrue(criteria.getVerifiedOnly());
        assertEquals(AvailabilityStatus.BUSY, criteria.getAvailabilityStatus());
        assertNull(criteria.getMinHourlyRate());
        assertNull(criteria.getMaxHourlyRate());
    }
}
