package com.eadl.connect_backend.infrastructure.adapter.out.persistence.entity;

import lombok.*;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idReview;

    private Integer rating;

    @Column(length = 2048)
    private String comment;

    private LocalDateTime createdAt;

    // ManyToOne -> client (author of the review)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_client")
    private UserEntity client;

    // ManyToOne -> technician being reviewed
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_technician")
    private UserEntity technician;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
