package com.eadl.connect_backend.infrastructure.adapter.out.persistence.entity;

import lombok.*;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import com.eadl.connect_backend.domain.model.review.Rating;
import com.eadl.connect_backend.infrastructure.adapter.out.persistence.mapper.RatingJpaConverter;

@Entity
@Table(name = "reviews")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idReview;

    @Convert(converter = RatingJpaConverter.class)
    @Column(nullable = false)
    private Rating rating;

    @Column(nullable = true, length = 2048)
    private String comment;

    private LocalDateTime createdAt;

    // ManyToOne -> client (author of the review)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_client")
    private UserEntity client;

    // OneToOne -> reservation, being reviewed
    @OneToOne(mappedBy = "review", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private ReservationEntity reservation;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
