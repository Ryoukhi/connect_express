package com.eadl.connect_backend.infrastructure.adapter.out.persistence.entity;

import java.math.BigDecimal;

import com.eadl.connect_backend.domain.model.technician.AvailabilityStatus;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class TechnicianSearchCriteriaEnitity {
    
    private String city;
    private Boolean activeOnly;
    private Boolean verifiedOnly;
    private AvailabilityStatus availabilityStatus;
    private BigDecimal minHourlyRate;
    private BigDecimal maxHourlyRate;
    private Long categoryId;

}