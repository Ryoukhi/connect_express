package com.eadl.connect_backend.domain.model.technician;

public enum AvailabilityStatus {
    AVAILABLE,      // Disponible pour de nouvelles missions
    BUSY,          // Occupé (en intervention)
    UNAVAILABLE,   // Indisponible (hors ligne)
    ON_BREAK       // En pause
}