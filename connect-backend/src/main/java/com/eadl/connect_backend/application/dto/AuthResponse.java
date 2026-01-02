package com.eadl.connect_backend.application.dto;

public class AuthResponse {

    private String token;
    private Long userId;
    private String firsName;
    private String lastName;
    private boolean active;

        
    public AuthResponse(String token, Long userId, String firsName, String lastName, boolean active) {
        this.token = token;
        this.userId = userId;
        this.firsName = firsName;
        this.lastName = lastName;
        this.active = active;
    }

    // Getters and Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFirsName() {
        return firsName;
    }

    public void setFirsName(String firsName) {
        this.firsName = firsName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
