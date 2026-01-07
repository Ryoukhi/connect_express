package com.eadl.connect_backend.domain.model.review;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class ReviewTest {

    @Test
    void shouldCreateReviewWithValidData() {
        Review review = Review.create(
                1L,
                10L,
                20L,
                5,
                "Excellent service"
        );

        assertNotNull(review);
        assertEquals(10L, review.getIdClient());
        assertEquals(5, review.getRating().getValue());
        assertEquals("Excellent service", review.getComment());
        assertNotNull(review.getCreatedAt());
    }

    @Test
    void shouldDetectPositiveReview() {
        Review review = Review.create(
                1L,
                10L,
                20L,
                4,
                "Bon travail"
        );

        assertTrue(review.isPositive());
        assertFalse(review.isNegative());
    }

    @Test
    void shouldDetectNegativeReview() {
        Review review = Review.create(
                1L,
                10L,
                20L,
                2,
                "Très mauvais"
        );

        assertTrue(review.isNegative());
        assertFalse(review.isPositive());
    }

    @Test
    void shouldUpdateReview() {
        Review review = Review.create(
                1L,
                10L,
                20L,
                3,
                "Correct"
        );

        review.updateReview(5, "Excellent après correction");

        assertEquals(5, review.getRating().getValue());
        assertEquals("Excellent après correction", review.getComment());
    }

    @Test
    void shouldThrowExceptionWhenUpdatingWithInvalidRating() {
        Review review = Review.create(
                1L,
                10L,
                20L,
                4,
                "Bon"
        );

        assertThrows(IllegalArgumentException.class, () ->
                review.updateReview(6, "Note invalide")
        );
    }

    @Test
    void shouldRespectEntityEqualityById() {
        Review review1 = new Review();
        review1.setIdReview(1L);

        Review review2 = new Review();
        review2.setIdReview(1L);

        assertEquals(review1, review2);
        assertEquals(review1.hashCode(), review2.hashCode());
    }

    @Test
    void shouldNotBeEqualIfIdsAreDifferent() {
        Review review1 = new Review();
        review1.setIdReview(1L);

        Review review2 = new Review();
        review2.setIdReview(2L);

        assertNotEquals(review1, review2);
    }

    @Test
    void shouldAllowReconstructionFromDatabase() {
        Review review = new Review();
        LocalDateTime now = LocalDateTime.now();

        review.setIdReview(5L);
        review.setIdClient(99L);
        review.setRating(Rating.of(4));
        review.setComment("Bon service");
        review.setCreatedAt(now);

        assertEquals(5L, review.getIdReview());
        assertEquals(99L, review.getIdClient());
        assertEquals(4, review.getRating().getValue());
        assertEquals("Bon service", review.getComment());
        assertEquals(now, review.getCreatedAt());
    }
}
