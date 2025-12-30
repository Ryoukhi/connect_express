package com.eadl.connect_backend.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.eadl.connect_backend.domain.model.technician.AvailabilityStatus;

@Entity
@Table(name = "technician_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TechnicianProfileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProfile;

    private String bio;

    private Integer yearsExperience;

    private BigDecimal hourlyRate;

    private AvailabilityStatus availabilityStatus;

    private Integer completedJobs;

    private BigDecimal averageRating;

    private boolean verified;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // Owner technician (one-to-one to user)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_technician")
    private UserEntity technician;

    // Documents associated with this profile
    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TechnicianDocumentEntity> documents = new ArrayList<>();

    // Skills associated with this profile
    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TechnicianSkillEntity> skills = new ArrayList<>();


}
