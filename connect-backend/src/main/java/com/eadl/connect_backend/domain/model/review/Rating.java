package com.eadl.connect_backend.domain.model.review;

import java.util.Objects;

/**
 * Rating - Value Object représentant une note de 1 à 5
 */
public class Rating {
    private final int value;

    public Rating(int value) {
        if (value < 1 || value > 5) {
            throw new IllegalArgumentException("La note doit être entre 1 et 5");
        }
        this.value = value;
    }

    // ========== Factory Method ==========
    public static Rating of(int value) {
        return new Rating(value);
    }

    // ========== Business Logic Methods ==========
    public boolean isExcellent() {
        return value >= 4;
    }

    public boolean isPoor() {
        return value <= 2;
    }

    public String getLabel() {
        return switch (value) {
            case 1 -> "Très mauvais";
            case 2 -> "Mauvais";
            case 3 -> "Moyen";
            case 4 -> "Bon";
            case 5 -> "Excellent";
            default -> "Inconnu";
        };
    }

    // ========== Getter ==========
    public int getValue() {
        return value;
    }

    // ========== equals & hashCode (Value Object) ==========
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rating rating = (Rating) o;
        return value == rating.value;
    }

    public static Rating fromValue(Integer value) {
        if (value == null) {
            throw new IllegalArgumentException("Rating cannot be null");
        }
        return new Rating(value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value + "/5 - " + getLabel();
    }
}
