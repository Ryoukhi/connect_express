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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@Tag(name = "Avis", description = "Gestion des avis/évaluations clients")
public class ReviewController {

        private final ReviewService reviewService;
        private final ReviewMapper reviewMapper;
        private final CurrentUserProvider currentUserProvider;

        // ================== CREATE ==================
        @Operation(summary = "Créer un avis (CLIENT)")
        @ApiResponses({
                        @ApiResponse(responseCode = "201", description = "Avis créé", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReviewDto.class))),
                        @ApiResponse(responseCode = "400", description = "Données invalides"),
                        @ApiResponse(responseCode = "403", description = "Accès interdit")
        })
        @PreAuthorize("hasRole('CLIENT')")
        @PostMapping
        public ResponseEntity<ReviewDto> createReview(@RequestBody ReviewDto dto) {
                Long clientId = currentUserProvider.getCurrentUserId();
                Review created = reviewService.createReview(clientId, dto.getIdReservation(), dto.getRating(),
                                dto.getComment());
                return ResponseEntity.status(HttpStatus.CREATED).body(reviewMapper.toDto(created));
        }

        // ================== READ ==================
        @Operation(summary = "Récupérer un avis par ID")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Avis trouvé", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReviewDto.class))),
                        @ApiResponse(responseCode = "404", description = "Avis non trouvé"),
                        @ApiResponse(responseCode = "403", description = "Accès interdit")
        })
        @PreAuthorize("hasAnyRole('CLIENT','ADMIN')")
        @GetMapping("/{id}")
        public ResponseEntity<ReviewDto> getReviewById(@Parameter(description = "ID de l'avis") @PathVariable Long id) {
                return reviewService.getReviewById(id)
                                .map(reviewMapper::toDto)
                                .map(ResponseEntity::ok)
                                .orElse(ResponseEntity.notFound().build());
        }

        @Operation(summary = "Récupérer les avis du client connecté")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Liste des avis"),
                        @ApiResponse(responseCode = "403", description = "Accès interdit")
        })
        @PreAuthorize("hasRole('CLIENT')")
        @GetMapping("/me")
        public ResponseEntity<List<ReviewDto>> getMyReviews() {
                Long clientId = currentUserProvider.getCurrentUserId();
                return ResponseEntity.ok(
                                reviewService.getClientReviews(clientId).stream().map(reviewMapper::toDto).toList());
        }

        @Operation(summary = "Récupérer les avis d’un client (ADMIN)")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Liste des avis du client"),
                        @ApiResponse(responseCode = "403", description = "Accès interdit")
        })
        @PreAuthorize("hasRole('ADMIN')")
        @GetMapping("/client/{clientId}")
        public ResponseEntity<List<ReviewDto>> getClientReviews(
                        @Parameter(description = "ID du client") @PathVariable Long clientId) {
                return ResponseEntity.ok(
                                reviewService.getClientReviews(clientId).stream().map(reviewMapper::toDto).toList());
        }

        // ================== UPDATE ==================
        @Operation(summary = "Modifier un avis (CLIENT)")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Avis mis à jour", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReviewDto.class))),
                        @ApiResponse(responseCode = "404", description = "Avis non trouvé"),
                        @ApiResponse(responseCode = "403", description = "Accès interdit")
        })
        @PreAuthorize("hasRole('CLIENT')")
        @PutMapping("/{id}")
        public ResponseEntity<ReviewDto> updateReview(
                        @Parameter(description = "ID de l'avis") @PathVariable Long id,
                        @RequestBody ReviewDto dto) {
                Review updated = reviewMapper.toModel(dto);
                Review result = reviewService.updateReview(id, updated);
                return ResponseEntity.ok(reviewMapper.toDto(result));
        }

        // ================== DELETE ==================
        @Operation(summary = "Supprimer un avis (ADMIN)")
        @ApiResponses({
                        @ApiResponse(responseCode = "204", description = "Avis supprimé"),
                        @ApiResponse(responseCode = "404", description = "Avis non trouvé"),
                        @ApiResponse(responseCode = "403", description = "Accès interdit")
        })
        @PreAuthorize("hasRole('ADMIN')")
        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteReview(
                        @Parameter(description = "ID de l'avis") @PathVariable Long id,
                        @Parameter(description = "Raison de la suppression") @RequestParam String reason) {
                Long adminId = currentUserProvider.getCurrentUserId();
                reviewService.deleteReview(adminId, id, reason);
                return ResponseEntity.noContent().build();
        }

        // ================== STATS ==================
        @Operation(summary = "Compter le nombre total d’avis (ADMIN)")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Nombre total d'avis"),
                        @ApiResponse(responseCode = "403", description = "Accès interdit")
        })
        @PreAuthorize("hasRole('ADMIN')")
        @GetMapping("/stats/count")
        public ResponseEntity<Long> countReviews() {
                return ResponseEntity.ok(reviewService.countReviews());
        }

        @Operation(summary = "Récupérer l'avis pour une réservation spécifique (CLIENT)")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Avis trouvé", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReviewDto.class))),
                        @ApiResponse(responseCode = "404", description = "Avis non trouvé"),
                        @ApiResponse(responseCode = "403", description = "Accès interdit")
        })
        @PreAuthorize("hasAnyRole('CLIENT', 'TECHNICIAN')")
        @GetMapping("/reservation/{reservationId}")
        public ResponseEntity<ReviewDto> getReviewForReservation(
                        @Parameter(description = "ID de la réservation") @PathVariable Long reservationId) {
                return reviewService.getReviewByReservationId(reservationId)
                                .map(reviewMapper::toDto)
                                .map(ResponseEntity::ok)
                                .orElse(ResponseEntity.notFound().build());
        }
}
