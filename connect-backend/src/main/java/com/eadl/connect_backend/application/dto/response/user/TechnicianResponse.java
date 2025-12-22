package com.eadl.connect_backend.application.dto.response.user;

import com.eadl.connect_backend.application.dto.response.profile.TechnicianProfileResponse;

/**
 * DTO pour la réponse technicien avec profil
 */
public class TechnicianResponse extends UserResponse {
    
    private TechnicianProfileResponse profile;
    
    // Getters & Setters
    public TechnicianProfileResponse getProfile() {
        return profile;
    }
    
    public void setProfile(TechnicianProfileResponse profile) {
        this.profile = profile;
    }
}