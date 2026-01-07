package com.eadl.connect_backend.domain.port.in.technician;

import java.math.BigDecimal;
import com.eadl.connect_backend.domain.model.technician.AvailabilityStatus;

/**
 * Criteria for searching technicians (domain-level)
 */
public class TechnicianSearchCriteria {

    private String city;
    private String neighborhood;
    private String categoryName;
    private AvailabilityStatus availabilityStatus;
    private BigDecimal minRating;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;

    public TechnicianSearchCriteria() {}

    public TechnicianSearchCriteria(String city, String neighborhood, String categoryName,
            AvailabilityStatus availabilityStatus, BigDecimal minRating, BigDecimal minPrice,
            BigDecimal maxPrice) {
        this.city = city;
        this.neighborhood = neighborhood;
        this.categoryName = categoryName;
        this.availabilityStatus = availabilityStatus;
        this.minRating = minRating;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }

    public String getCity() {
        return city;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public AvailabilityStatus getAvailabilityStatus() {
        return availabilityStatus;
    }

    public BigDecimal getMinRating() {
        return minRating;
    }

    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public BigDecimal getMaxPrice() {
        return maxPrice;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setAvailabilityStatus(AvailabilityStatus availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }

    public void setMinRating(BigDecimal minRating) {
        this.minRating = minRating;
    }

    public void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
    }

    public void setMaxPrice(BigDecimal maxPrice) {
        this.maxPrice = maxPrice;
    }
}