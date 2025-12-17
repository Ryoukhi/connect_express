package com.eadl.connect_backend.application.dto.request.auth;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO pour la requête de rafraîchissement de token
 */
public class RefreshTokenRequest {
    
    @NotBlank(message = "Le refresh token est obligatoire")
    private String refreshToken;
    
    // Constructeurs
    public RefreshTokenRequest() {}
    
    public RefreshTokenRequest(String refreshToken) {
        this.refreshToken = refreshToken;
    }
    
    // Getters & Setters
    public String getRefreshToken() {
        return refreshToken;
    }
    
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}