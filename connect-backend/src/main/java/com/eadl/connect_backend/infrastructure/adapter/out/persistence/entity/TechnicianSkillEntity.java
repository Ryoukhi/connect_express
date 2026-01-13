package com.eadl.connect_backend.infrastructure.adapter.out.persistence.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.eadl.connect_backend.domain.model.technician.AvailabilityStatus;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "technician_skills")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TechnicianSkillEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSkill;

    private String name;

    private String description;

    private Integer level; // 1 à 5 (débutant à expert)

    private Integer yearsExperience;

    private BigDecimal hourlyRate;

    private AvailabilityStatus availabilityStatus;

    private boolean verified;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // Documents associated with this skill
    @OneToMany(mappedBy = "skill", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TechnicianDocumentEntity> documentsEntities;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user")
    private UserEntity technician;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_category")
    private CategoryEntity category;


}
