package com.eadl.connect_backend.application.dto;

import com.eadl.connect_backend.domain.model.technician.AvailabilityStatus;

public class TechnicianResultSearchDto {
    private Long id;
    private String name;
    private boolean verified;
    private double averageRating;
    private AvailabilityStatus availabilityStatus;
    private double hourlyRate;
    private String skillName;
    private int yearsOfExperience;
    private String city;
    private String neighborhood;
    private String profilePhotoUrl;

    public TechnicianResultSearchDto(Long id, String name, boolean verified, double averageRating,
            AvailabilityStatus availabilityStatus, double hourlyRate, String skillName, int yearsOfExperience,
            String city, String neighborhood, String profilePhotoUrl) {
        this.id = id;
        this.name = name;
        this.verified = verified;
        this.averageRating = averageRating;
        this.availabilityStatus = availabilityStatus;
        this.hourlyRate = hourlyRate;
        this.skillName = skillName;
        this.yearsOfExperience = yearsOfExperience;
        this.city = city;
        this.neighborhood = neighborhood;
        this.profilePhotoUrl = profilePhotoUrl;
    }

    public Long getId() {
        return id;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public boolean isVerified() {
        return verified;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public AvailabilityStatus getAvailabilityStatus() {
        return availabilityStatus;
    }

    public double getHourlyRate() {
        return hourlyRate;
    }

    public String getSkillName() {
        return skillName;
    }

    public int getYearsOfExperience() {
        return yearsOfExperience;
    }

    public String getCity() {
        return city;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public String getProfilePhotoUrl() {
        return profilePhotoUrl;
    }
}