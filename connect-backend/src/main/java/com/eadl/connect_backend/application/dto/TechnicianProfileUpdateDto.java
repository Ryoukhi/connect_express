package com.eadl.connect_backend.application.dto;

import java.math.BigDecimal;

public class TechnicianProfileUpdateDto {
private String bio;
    private Long idCategory;
    private Integer yearsExperience;
    private BigDecimal hourlyRate;
    
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
    
}
