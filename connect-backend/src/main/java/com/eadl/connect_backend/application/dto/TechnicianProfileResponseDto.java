package com.eadl.connect_backend.application.dto;

import java.math.BigDecimal;

import com.eadl.connect_backend.domain.model.technician.AvailabilityStatus;

public class TechnicianProfileResponseDto {

    private Long id;
    private Long technicianId;
    private String bio;
    private Long idCategory;
    private Integer yearsExperience;
    private BigDecimal hourlyRate;
    private AvailabilityStatus availabilityStatus;
    private boolean verified;
    private int completedJobs;
    private BigDecimal averageRating;

    public TechnicianProfileResponseDto() {}

    public TechnicianProfileResponseDto(
            Long id,
            Long technicianId,
            String bio,
            Long idCategory,
            Integer yearsExperience,
            BigDecimal hourlyRate,
            AvailabilityStatus availabilityStatus,
            boolean verified,
            int completedJobs,
            BigDecimal averageRating
    ) {
        this.id = id;
        this.technicianId = technicianId;
        this.bio = bio;
        this.idCategory = idCategory;
        this.yearsExperience = yearsExperience;
        this.hourlyRate = hourlyRate;
        this.availabilityStatus = availabilityStatus;
        this.verified = verified;
        this.completedJobs = completedJobs;
        this.averageRating = averageRating;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getTechnicianId() {
        return technicianId;
    }
    public void setTechnicianId(Long technicianId) {
        this.technicianId = technicianId;
    }
    public String getBio() {
        return bio;
    }
    public void setBio(String bio) {
        this.bio = bio;
    }
    public Long getIdCategory() {
        return idCategory;
    }
    public void setIdCategory(Long idCategory) {
        this.idCategory = idCategory;
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
    public int getCompletedJobs() {
        return completedJobs;
    }
    public void setCompletedJobs(int completedJobs) {
        this.completedJobs = completedJobs;
    }
    public BigDecimal getAverageRating() {
        return averageRating;
    }
    public void setAverageRating(BigDecimal averageRating) {
        this.averageRating = averageRating;
    }

    
}