package com.eadl.connect_backend.application.dto.request.search;

import java.math.BigDecimal;

/**
 * DTO pour les critères de recherche de techniciens
 */
public class TechnicianSearchCriteria {
    
    private Long categoryId;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private Double radiusKm;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private BigDecimal minRating;
    private Boolean availableOnly;
    
    // Getters & Setters
    public Long getCategoryId() {
        return categoryId;
    }
    
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
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
    
    public Double getRadiusKm() {
        return radiusKm;
    }
    
    public void setRadiusKm(Double radiusKm) {
        this.radiusKm = radiusKm;
    }
    
    public BigDecimal getMinPrice() {
        return minPrice;
    }
    
    public void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
    }
    
    public BigDecimal getMaxPrice() {
        return maxPrice;
    }
    
    public void setMaxPrice(BigDecimal maxPrice) {
        this.maxPrice = maxPrice;
    }
    
    public BigDecimal getMinRating() {
        return minRating;
    }
    
    public void setMinRating(BigDecimal minRating) {
        this.minRating = minRating;
    }
    
    public Boolean getAvailableOnly() {
        return availableOnly;
    }
    
    public void setAvailableOnly(Boolean availableOnly) {
        this.availableOnly = availableOnly;
    }
}