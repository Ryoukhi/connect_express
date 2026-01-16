package com.eadl.connect_backend.infrastructure.adapter.out.persistence.entity;

import com.eadl.connect_backend.domain.model.user.Role;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUser;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    private String phone;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String neighborhood;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private boolean active = true;

    private boolean emailVerified = false;

    private boolean phoneVerified = false;

    private String profilePhotoUrl;

    // Relations

    // Reviews written by a client (one client can write many reviews)
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewEntity> reviews = new ArrayList<>();

    // Reservations where user is the client (one client can request many reservations)
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReservationEntity> reservationsAsClient = new ArrayList<>();

    // Technician profile - one-to-one (a technician has one profile)
    @OneToOne(mappedBy = "technician", cascade = CascadeType.ALL, orphanRemoval = true)
    private TechnicianSkillEntity technicianSkill;

    // ========== JPA Lifecycle ==========
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
