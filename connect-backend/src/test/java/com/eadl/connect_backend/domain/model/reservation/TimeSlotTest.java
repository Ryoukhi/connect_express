package com.eadl.connect_backend.domain.model.reservation;

import static org.junit.jupiter.api.Assertions.*;

import java.time.DayOfWeek;
import java.time.LocalTime;

import org.junit.jupiter.api.Test;

class TimeSlotTest {

    @Test
    void shouldCreateValidTimeSlot() {
        TimeSlot slot = TimeSlot.of(
                DayOfWeek.MONDAY,
                LocalTime.of(8, 0),
                LocalTime.of(12, 0)
        );

        assertEquals(DayOfWeek.MONDAY, slot.getDayOfWeek());
        assertEquals(240, slot.getDurationInMinutes());
    }

    @Test
    void shouldDetectOverlap() {
        TimeSlot slot1 = TimeSlot.of(
                DayOfWeek.MONDAY,
                "08:00",
                "12:00"
        );

        TimeSlot slot2 = TimeSlot.of(
                DayOfWeek.MONDAY,
                "10:00",
                "14:00"
        );

        assertTrue(slot1.overlaps(slot2));
    }

    @Test
    void shouldNotOverlapDifferentDays() {
        TimeSlot slot1 = TimeSlot.of(
                DayOfWeek.MONDAY,
                "08:00",
                "12:00"
        );

        TimeSlot slot2 = TimeSlot.of(
                DayOfWeek.TUESDAY,
                "08:00",
                "12:00"
        );

        assertFalse(slot1.overlaps(slot2));
    }

    @Test
    void shouldThrowExceptionIfStartAfterEnd() {
        assertThrows(IllegalArgumentException.class, () ->
                TimeSlot.of(
                        DayOfWeek.MONDAY,
                        "14:00",
                        "10:00"
                )
        );
    }
}
