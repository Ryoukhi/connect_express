package com.eadl.connect_backend.domain.model.technician;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Profil professionnel du technicien
 */
public class TechnicianProfile {
    private Long idProfile;
    private Long idTechnician;
    private Long idCategory;
    private String bio;
    private Integer yearsExperience;
    private BigDecimal hourlyRate;
    private boolean verified;
    private AvailabilityStatus availabilityStatus;
    private Integer completedJobs;
    private BigDecimal averageRating;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public TechnicianProfile() {}

    // ========== Factory Method ==========
    public static TechnicianProfile create(Long idTechnician, String bio, 
                                          Integer yearsExperience, BigDecimal hourlyRate) {
        TechnicianProfile profile = new TechnicianProfile();
        profile.idTechnician = idTechnician;
        profile.bio = bio;
        profile.yearsExperience = yearsExperience;
        profile.hourlyRate = hourlyRate;
        profile.verified = false;
        profile.availabilityStatus = AvailabilityStatus.UNAVAILABLE;
        profile.completedJobs = 0;
        profile.averageRating = BigDecimal.ZERO;
        return profile;
    }

    
    // ========== Business Logic Methods ==========
    public void updateProfile(String bio, Integer yearsExperience, BigDecimal hourlyRate) {
        this.bio = bio;
        this.yearsExperience = yearsExperience;
        this.hourlyRate = hourlyRate;
    }

    public void verify() {
        this.verified = true;
    }

    public void setAvailable() {
        this.availabilityStatus = AvailabilityStatus.AVAILABLE;
    }

    public void setBusy() {
        this.availabilityStatus = AvailabilityStatus.BUSY;
    }

    public void setUnavailable() {
        this.availabilityStatus = AvailabilityStatus.UNAVAILABLE;
    }

    public void setOnBreak() {
        this.availabilityStatus = AvailabilityStatus.ON_BREAK;
    }

    public void incrementCompletedJobs() {
        this.completedJobs++;
    }

    public void updateAverageRating(BigDecimal newRating) {
        this.averageRating = newRating;
    }

    public boolean isAvailable() {
        return this.availabilityStatus == AvailabilityStatus.AVAILABLE;
    }

    public boolean isVerified() {
        return this.verified;
    }

    // ========== Getters ==========
    public Long getIdProfile() {
        return idProfile;
    }

    public Long getIdTechnician() {
        return idTechnician;
    }

    public String getBio() {
        return bio;
    }

    public Integer getYearsExperience() {
        return yearsExperience;
    }

    public BigDecimal getHourlyRate() {
        return hourlyRate;
    }

    public boolean getVerified() {
        return verified;
    }

    public AvailabilityStatus getAvailabilityStatus() {
        return availabilityStatus;
    }

    public Integer getCompletedJobs() {
        return completedJobs;
    }

    public BigDecimal getAverageRating() {
        return averageRating;
    }

    

    // ========== Setters (pour reconstruction depuis DB) ==========
    public void setIdProfile(Long idProfile) {
        this.idProfile = idProfile;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public void setCompletedJobs(Integer completedJobs) {
        this.completedJobs = completedJobs;
    }

    public void setAverageRating(BigDecimal averageRating) {
        this.averageRating = averageRating;
    }

    // ========== equals & hashCode ==========
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TechnicianProfile that = (TechnicianProfile) o;
        return Objects.equals(idProfile, that.idProfile);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idProfile);
    }

    @Override
    public String toString() {
        return "TechnicianProfile{" +
                "idProfile=" + idProfile +
                ", idTechnician=" + idTechnician +
                ", verified=" + verified +
                ", availabilityStatus=" + availabilityStatus +
                ", completedJobs=" + completedJobs +
                ", averageRating=" + averageRating +
                '}';
    }

    public void setIdTechnician(Long idTechnician) {
        this.idTechnician = idTechnician;
    }

    public Long getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(Long idCategory) {
        this.idCategory = idCategory;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setYearsExperience(Integer yearsExperience) {
        this.yearsExperience = yearsExperience;
    }

    public void setHourlyRate(BigDecimal hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public void setAvailabilityStatus(AvailabilityStatus availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
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