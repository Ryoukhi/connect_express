package com.eadl.connect_backend.infrastructure.adapter.in.web.controller.review;

import lombok.RequiredArgsConstructor;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.eadl.connect_backend.application.dto.ReviewDto;
import com.eadl.connect_backend.application.mapper.ReviewMapper;
import com.eadl.connect_backend.domain.model.review.Review;
import com.eadl.connect_backend.domain.port.in.review.ReviewService;
import com.eadl.connect_backend.domain.port.out.security.CurrentUserProvider;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final ReviewMapper reviewMapper;
    private final CurrentUserProvider currentUserProvider;

    // ================== CREATE ==================

    /**
     * Créer un avis (CLIENT)
     */
    @PreAuthorize("hasRole('CLIENT')")
    @PostMapping
    public ResponseEntity<ReviewDto> createReview(
            @RequestBody ReviewDto dto
    ) {
        Long clientId = currentUserProvider.getCurrentUserId();

        Review created = reviewService.createReview(
                clientId,
                dto.getRating(),
                dto.getComment()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(reviewMapper.toDto(created));
    }

    // ================== READ ==================

    /**
     * Récupérer un avis par ID
     */
    @PreAuthorize("hasAnyRole('CLIENT','ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<ReviewDto> getReviewById(@PathVariable Long id) {

        return reviewService.getReviewById(id)
                .map(reviewMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Récupérer les avis du client connecté
     */
    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/me")
    public ResponseEntity<List<ReviewDto>> getMyReviews() {

        Long clientId = currentUserProvider.getCurrentUserId();

        return ResponseEntity.ok(
                reviewService.getClientReviews(clientId)
                        .stream()
                        .map(reviewMapper::toDto)
                        .toList()
        );
    }

    /**
     * Récupérer les avis d’un client (ADMIN)
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<ReviewDto>> getClientReviews(
            @PathVariable Long clientId
    ) {
        return ResponseEntity.ok(
                reviewService.getClientReviews(clientId)
                        .stream()
                        .map(reviewMapper::toDto)
                        .toList()
        );
    }

    // ================== UPDATE ==================

    /**
     * Modifier un avis (CLIENT)
     */
    @PreAuthorize("hasRole('CLIENT')")
    @PutMapping("/{id}")
    public ResponseEntity<ReviewDto> updateReview(
            @PathVariable Long id,
            @RequestBody ReviewDto dto
    ) {
        Review updated = reviewMapper.toModel(dto);

        Review result = reviewService.updateReview(id, updated);

        return ResponseEntity.ok(reviewMapper.toDto(result));
    }

    // ================== DELETE ==================

    /**
     * Supprimer un avis (ADMIN)
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable Long id,
            @RequestParam String reason
    ) {
        Long adminId = currentUserProvider.getCurrentUserId();

        reviewService.deleteReview(adminId, id, reason);

        return ResponseEntity.noContent().build();
    }

    // ================== STATS ==================

    /**
     * Compter le nombre total d’avis
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/stats/count")
    public ResponseEntity<Long> countReviews() {
        return ResponseEntity.ok(reviewService.countReviews());
    }

    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/reservation/{reservationId}")
    public ResponseEntity<ReviewDto> getReviewForReservation(
            @PathVariable Long reservationId
    ) {
        Long clientId = currentUserProvider.getCurrentUserId();

        return reviewService
                .getReviewByClientAndReservation(clientId, reservationId)
                .map(reviewMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}