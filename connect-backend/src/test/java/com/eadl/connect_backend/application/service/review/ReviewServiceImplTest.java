package com.eadl.connect_backend.application.service.review;

import com.eadl.connect_backend.domain.model.review.Rating;
import com.eadl.connect_backend.domain.model.review.Review;
import com.eadl.connect_backend.domain.port.out.persistence.ReviewRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceImplTest {

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private ReviewServiceImpl reviewService;

    private Review review;

    @BeforeEach
    void setUp() {
        review = new Review();
        review.setIdClient(1L);
        review.setRating(new Rating(5));
        review.setComment("Excellent service");
    }

    // ================= CREATE =================

    @Test
    void shouldCreateReviewSuccessfully() {
        when(reviewRepository.save(any(Review.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Review result =
                reviewService.createReview(1L, new Rating(5), "Excellent service");

        assertThat(result).isNotNull();
        assertThat(result.getRating().getValue()).isEqualTo(5);
        assertThat(result.getCreatedAt()).isNotNull();
    }

    @Test
    void shouldThrowIfRatingIsNull() {
        assertThatThrownBy(() ->
                reviewService.createReview(1L, null, "comment"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("note est obligatoire");
    }

    @Test
    void shouldThrowIfRatingIsOutOfRange() {
        assertThatThrownBy(() ->
                reviewService.createReview(1L, new Rating(6), "comment"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("entre 1 et 5");
    }

    // ================= READ =================

    @Test
    void shouldReturnReviewById() {
        when(reviewRepository.findById(1L))
                .thenReturn(Optional.of(review));

        Optional<Review> result = reviewService.getReviewById(1L);

        assertThat(result).isPresent();
    }

    @Test
    void shouldReturnClientReviews() {
        when(reviewRepository.findByClientId(1L))
                .thenReturn(List.of(review));

        List<Review> reviews =
                reviewService.getClientReviews(1L);

        assertThat(reviews).hasSize(1);
    }

    @Test
    void shouldReturnReviewByClientAndReservation() {
        when(reviewRepository.findByClientIdAndReservationId(1L, 10L))
                .thenReturn(Optional.of(review));

        Optional<Review> result =
                reviewService.getReviewByClientAndReservation(1L, 10L);

        assertThat(result).isPresent();
    }

    @Test
    void shouldThrowIfClientOrReservationIdIsNull() {
        assertThatThrownBy(() ->
                reviewService.getReviewByClientAndReservation(null, 1L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    // ================= UPDATE =================

    @Test
    void shouldUpdateReviewSuccessfully() {
        when(reviewRepository.findById(1L))
                .thenReturn(Optional.of(review));

        when(reviewRepository.update(eq(1L), any()))
                .thenAnswer(invocation -> invocation.getArgument(1));

        Review updated = new Review();
        updated.setRating(new Rating(4));
        updated.setComment("Bon service");

        Review result = reviewService.updateReview(1L, updated);

        assertThat(result.getRating().getValue()).isEqualTo(4);
    }

    @Test
    void shouldThrowIfReviewNotFoundOnUpdate() {
        when(reviewRepository.findById(1L))
                .thenReturn(Optional.empty());

        Review updated = new Review();
        updated.setRating(new Rating(4));

        assertThatThrownBy(() ->
                reviewService.updateReview(1L, updated))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Avis introuvable");
    }

    @Test
    void shouldThrowIfUpdatedRatingIsInvalid() {
        when(reviewRepository.findById(1L))
                .thenReturn(Optional.of(review));

        Review updated = new Review();
        updated.setRating(null);

        assertThatThrownBy(() ->
                reviewService.updateReview(1L, updated))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("note est obligatoire");
    }

    // ================= DELETE =================

    @Test
    void shouldDeleteReviewSuccessfully() {
        when(reviewRepository.findById(1L))
                .thenReturn(Optional.of(review));

        reviewService.deleteReview(99L, 1L, "Contenu inapproprié");

        verify(reviewRepository).delete(review);
    }

    @Test
    void shouldThrowIfDeleteReasonIsMissing() {
        assertThatThrownBy(() ->
                reviewService.deleteReview(1L, 1L, " "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("raison est obligatoire");
    }

    @Test
    void shouldThrowIfReviewNotFoundOnDelete() {
        when(reviewRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                reviewService.deleteReview(1L, 1L, "raison"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    // ================= COUNT =================

    @Test
    void shouldCountReviews() {
        when(reviewRepository.count()).thenReturn(3L);

        Long count = reviewService.countReviews();

        assertThat(count).isEqualTo(3L);
    }
}
