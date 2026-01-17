package com.eadl.connect_backend.domain.model.technician;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Compétence du technicien
 */
public class TechnicianSkill {
    private Long idSkill;

    private Long idUser;

    private Long idCategory;

    private String name;

    private String description;

    private Integer level; // 1 à 5 (débutant à expert)

    private Integer yearsExperience;

    private BigDecimal hourlyRate;

    private AvailabilityStatus availabilityStatus;

    private boolean verified;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    

    public TechnicianSkill(Long idSkill, Long idUser, Long idCategory, String name, String description, Integer level,
            Integer yearsExperience, BigDecimal hourlyRate, AvailabilityStatus availabilityStatus, boolean verified,
            LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.idSkill = idSkill;
        this.idUser = idUser;
        this.idCategory = idCategory;
        this.name = name;
        this.description = description;
        this.level = level;
        this.yearsExperience = yearsExperience;
        this.hourlyRate = hourlyRate;
        this.availabilityStatus = availabilityStatus;
        this.verified = verified;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public TechnicianSkill() {
    }

    // ========== Factory Method ==========
    public static TechnicianSkill create(Long idUser, Long idCategory,
                                        String name, String description,
                                        Integer level) {

        TechnicianSkill skill = new TechnicianSkill();
        skill.idUser = idUser;
        skill.idCategory = idCategory;
        skill.name = name;
        skill.description = description;
        skill.level = validateLevel(level);
        skill.createdAt = LocalDateTime.now();
        skill.verified = false;

        return skill;
    }

    // ========== Business Logic Methods ==========
    public void updateSkill(String name, String description, Integer level) {
        this.name = name;
        this.description = description;
        this.level = validateLevel(level);
        this.updatedAt = LocalDateTime.now();
    }

    public void upgradeLevel() {
        if (this.level != null && this.level < 5) {
            this.level++;
            this.updatedAt = LocalDateTime.now();
        }
    }

    public void downgradeLevel() {
        if (this.level != null && this.level > 1) {
            this.level--;
            this.updatedAt = LocalDateTime.now();
        }
    }

    private static Integer validateLevel(Integer level) {
        if (level == null || level < 1 || level > 5) {
            throw new IllegalArgumentException("Le niveau doit être entre 1 et 5");
        }
        return level;
    }

    public boolean isExpert() {
        return this.level != null && this.level >= 4;
    }

    public Long getIdSkill() {
        return idSkill;
    }

    public void setIdSkill(Long idSkill) {
        this.idSkill = idSkill;
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public Long getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(Long idCategory) {
        this.idCategory = idCategory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getYearsExperience() {
        return yearsExperience;
    }

    public void setYearsExperience(Integer yearsExperience) {
        this.yearsExperience = yearsExperience;
    }

    public BigDecimal getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(BigDecimal hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public AvailabilityStatus getAvailabilityStatus() {
        return availabilityStatus;
    }

    public void setAvailabilityStatus(AvailabilityStatus availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    /**
     * Confirme ou annule la vérification de la compétence.
     * Met à jour la date de modification.
     */
    public void verifySkill(boolean verified) {
        this.verified = verified;
        this.updatedAt = LocalDateTime.now();
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}