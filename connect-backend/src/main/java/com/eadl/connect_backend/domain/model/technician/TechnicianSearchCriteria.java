package com.eadl.connect_backend.domain.model.technician;

import java.math.BigDecimal;

public class TechnicianSearchCriteria {
    private String city;
    private Boolean activeOnly;
    private Boolean verifiedOnly;
    private AvailabilityStatus availabilityStatus;
    private BigDecimal minHourlyRate;
    private BigDecimal maxHourlyRate;
    private Long categoryId;
    
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public Boolean getActiveOnly() {
        return activeOnly;
    }
    public void setActiveOnly(Boolean activeOnly) {
        this.activeOnly = activeOnly;
    }
    public Boolean getVerifiedOnly() {
        return verifiedOnly;
    }
    public void setVerifiedOnly(Boolean verifiedOnly) {
        this.verifiedOnly = verifiedOnly;
    }
    public AvailabilityStatus getAvailabilityStatus() {
        return availabilityStatus;
    }
    public void setAvailabilityStatus(AvailabilityStatus availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }
    public BigDecimal getMinHourlyRate() {
        return minHourlyRate;
    }
    public void setMinHourlyRate(BigDecimal minHourlyRate) {
        this.minHourlyRate = minHourlyRate;
    }
    public BigDecimal getMaxHourlyRate() {
        return maxHourlyRate;
    }
    public void setMaxHourlyRate(BigDecimal maxHourlyRate) {
        this.maxHourlyRate = maxHourlyRate;
    }
    public Long getCategoryId() {
        return categoryId;
    }
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    
}