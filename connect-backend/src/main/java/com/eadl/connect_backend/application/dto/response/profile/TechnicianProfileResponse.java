package com.eadl.connect_backend.application.dto.response.profile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO pour la réponse profil technicien
 */
public class TechnicianProfileResponse {
    
    private Long idProfile;
    private Long idTechnician;
    private String bio;
    private Integer yearsExperience;
    private BigDecimal hourlyRate;
    private boolean verified;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String availabilityStatus;
    private LocalDateTime lastLocationUpdate;
    private String profilePhotoUrl;
    private Integer completedJobs;
    private BigDecimal averageRating;
    private List<TechnicianSkillResponse> skills;
    
    // Getters & Setters
    public Long getIdProfile() {
        return idProfile;
    }
    
    public void setIdProfile(Long idProfile) {
        this.idProfile = idProfile;
    }
    
    public Long getIdTechnician() {
        return idTechnician;
    }
    
    public void setIdTechnician(Long idTechnician) {
        this.idTechnician = idTechnician;
    }
    
    public String getBio() {
        return bio;
    }
    
    public void setBio(String bio) {
        this.bio = bio;
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
    
    public boolean isVerified() {
        return verified;
    }
    
    public void setVerified(boolean verified) {
        this.verified = verified;
    }
    
    public BigDecimal getLatitude() {
        return latitude;
    }
    
    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }
    
    public BigDecimal getLongitude() {
        return longitude;
    }
    
    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }
    
    public String getAvailabilityStatus() {
        return availabilityStatus;
    }
    
    public void setAvailabilityStatus(String availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }
    
    public LocalDateTime getLastLocationUpdate() {
        return lastLocationUpdate;
    }
    
    public void setLastLocationUpdate(LocalDateTime lastLocationUpdate) {
        this.lastLocationUpdate = lastLocationUpdate;
    }
    
    public String getProfilePhotoUrl() {
        return profilePhotoUrl;
    }
    
    public void setProfilePhotoUrl(String profilePhotoUrl) {
        this.profilePhotoUrl = profilePhotoUrl;
    }
    
    public Integer getCompletedJobs() {
        return completedJobs;
    }
    
    public void setCompletedJobs(Integer completedJobs) {
        this.completedJobs = completedJobs;
    }
    
    public BigDecimal getAverageRating() {
        return averageRating;
    }
    
    public void setAverageRating(BigDecimal averageRating) {
        this.averageRating = averageRating;
    }
    
    public List<TechnicianSkillResponse> getSkills() {
        return skills;
    }
    
    public void setSkills(List<TechnicianSkillResponse> skills) {
        this.skills = skills;
    }
}