package com.eadl.connect_backend.domain.model.review;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class RatingTest {

    @Test
    void shouldCreateValidRating() {
        Rating rating = Rating.of(4);

        assertEquals(4, rating.getValue());
        assertTrue(rating.isExcellent());
        assertFalse(rating.isPoor());
    }

    @Test
    void shouldDetectPoorRating() {
        Rating rating = Rating.of(2);

        assertTrue(rating.isPoor());
        assertFalse(rating.isExcellent());
    }

    @Test
    void shouldReturnCorrectLabel() {
        assertEquals("Très mauvais", Rating.of(1).getLabel());
        assertEquals("Mauvais", Rating.of(2).getLabel());
        assertEquals("Moyen", Rating.of(3).getLabel());
        assertEquals("Bon", Rating.of(4).getLabel());
        assertEquals("Excellent", Rating.of(5).getLabel());
    }

    @Test
    void shouldThrowExceptionIfRatingLessThanOne() {
        assertThrows(IllegalArgumentException.class, () ->
                Rating.of(0)
        );
    }

    @Test
    void shouldThrowExceptionIfRatingGreaterThanFive() {
        assertThrows(IllegalArgumentException.class, () ->
                Rating.of(6)
        );
    }

    @Test
    void shouldCreateRatingFromIntegerValue() {
        Rating rating = Rating.fromValue(3);

        assertEquals(3, rating.getValue());
    }

    @Test
    void shouldThrowExceptionIfFromValueIsNull() {
        assertThrows(IllegalArgumentException.class, () ->
                Rating.fromValue(null)
        );
    }

    @Test
    void shouldRespectValueObjectEquality() {
        Rating rating1 = Rating.of(5);
        Rating rating2 = Rating.of(5);

        assertEquals(rating1, rating2);
        assertEquals(rating1.hashCode(), rating2.hashCode());
    }

    @Test
    void shouldHaveMeaningfulToString() {
        Rating rating = Rating.of(5);

        assertEquals("5/5 - Excellent", rating.toString());
    }
}
