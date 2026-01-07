package com.eadl.connect_backend.application.dto;

import java.math.BigDecimal;

import com.eadl.connect_backend.domain.model.technician.AvailabilityStatus;

/**
 * DTO used for binding search query parameters in controller
 */
public class TechnicianSearchDto {

    private String city;
    private String neighborhood;
    private String categoryName;
    private AvailabilityStatus availabilityStatus;
    private BigDecimal minRating;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;

    public TechnicianSearchDto() {}

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public AvailabilityStatus getAvailabilityStatus() {
        return availabilityStatus;
    }

    public void setAvailabilityStatus(AvailabilityStatus availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }

    public BigDecimal getMinRating() {
        return minRating;
    }

    public void setMinRating(BigDecimal minRating) {
        this.minRating = minRating;
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
}