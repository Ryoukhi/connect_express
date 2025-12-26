package com.eadl.connect_backend.infrastructure.adapter.out.persistence.entity;

import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCategory;

    private String name;

    private String description;

    private String iconUrl;

    private boolean active;

    private Integer displayOrder;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private List<TechnicianSkillEntity> skills;

}
