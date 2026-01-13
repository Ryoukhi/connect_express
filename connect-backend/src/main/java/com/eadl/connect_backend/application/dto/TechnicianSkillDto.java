package com.eadl.connect_backend.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.eadl.connect_backend.domain.model.technician.AvailabilityStatus;

public class TechnicianSkillDto {

    private Long idSkill;
    private Long idUser;
    private Long idCategory;
    private String name;
    private String description;
    private Integer level; // 1 à 5
    private Integer yearsExperience;
    private BigDecimal hourlyRate;
    private AvailabilityStatus availabilityStatus;
    private boolean verified;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

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