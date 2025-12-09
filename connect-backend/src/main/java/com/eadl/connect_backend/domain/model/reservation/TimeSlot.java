package com.eadl.connect_backend.domain.model.reservation;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Objects;

/**
 * TimeSlot - Value Object représentant une plage horaire de disponibilité
 */
public class TimeSlot {
    private final DayOfWeek dayOfWeek;
    private final LocalTime startTime;
    private final LocalTime endTime;

    private TimeSlot(DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime) {
        if (startTime.isAfter(endTime)) {
            throw new IllegalArgumentException("L'heure de début doit être avant l'heure de fin");
        }
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // ========== Factory Methods ==========
    public static TimeSlot of(DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime) {
        return new TimeSlot(dayOfWeek, startTime, endTime);
    }

    public static TimeSlot of(DayOfWeek dayOfWeek, String startTime, String endTime) {
        return new TimeSlot(
            dayOfWeek, 
            LocalTime.parse(startTime), 
            LocalTime.parse(endTime)
        );
    }

    // ========== Business Logic Methods ==========
    public boolean overlaps(TimeSlot other) {
        if (!this.dayOfWeek.equals(other.dayOfWeek)) {
            return false;
        }
        return !this.endTime.isBefore(other.startTime) && 
               !this.startTime.isAfter(other.endTime);
    }

    public boolean contains(LocalTime time) {
        return !time.isBefore(startTime) && !time.isAfter(endTime);
    }

    public long getDurationInMinutes() {
        return java.time.Duration.between(startTime, endTime).toMinutes();
    }

    // ========== Getters ==========
    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    // ========== equals & hashCode (Value Object) ==========
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeSlot timeSlot = (TimeSlot) o;
        return dayOfWeek == timeSlot.dayOfWeek &&
               Objects.equals(startTime, timeSlot.startTime) &&
               Objects.equals(endTime, timeSlot.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dayOfWeek, startTime, endTime);
    }

    @Override
    public String toString() {
        return dayOfWeek + " " + startTime + "-" + endTime;
    }
}