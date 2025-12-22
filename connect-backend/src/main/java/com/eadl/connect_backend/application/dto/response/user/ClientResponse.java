package com.eadl.connect_backend.application.dto.response.user;

/**
 * DTO pour la réponse client
 */
public class ClientResponse extends UserResponse {
    
    private String address;
    
    // Getters & Setters
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
}