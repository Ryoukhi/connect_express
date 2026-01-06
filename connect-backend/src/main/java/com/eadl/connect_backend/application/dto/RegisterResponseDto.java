package com.eadl.connect_backend.application.dto;

public class RegisterResponseDto {

    public String token;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String city;
    private String neighborhood;
    private String profilePhotoUrl;

    public RegisterResponseDto() {
    }

    public RegisterResponseDto(String token, String firstName, String lastName, String email, String phone, String city,
            String neighborhood, String profilePhotoUrl) {
        this.token = token;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.city = city;
        this.neighborhood = neighborhood;
        this.profilePhotoUrl = profilePhotoUrl;
    }



    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
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
    public String getProfilePhotoUrl() {
        return profilePhotoUrl;
    }
    public void setProfilePhotoUrl(String profilePhotoUrl) {
        this.profilePhotoUrl = profilePhotoUrl;
    }
}
