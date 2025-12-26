package com.eadl.connect_backend.infrastructure.adapter.out.persistence.entity;

import lombok.*;


import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "admin_actions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminActionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAction;

    private String actionType;

    private String reason;

    private LocalDateTime createdAt;

    // Admin user who performed the action
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_admin")
    private UserEntity admin;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
