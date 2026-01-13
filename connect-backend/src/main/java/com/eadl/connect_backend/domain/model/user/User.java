package com.eadl.connect_backend.domain.model.user;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entité racine User - Classe abstraite
 * Représente un utilisateur de la plateforme
 */
public class User {
    protected Long idUser;
    protected String firstName;
    protected String lastName;
    protected String email;
    protected String phone;
    protected String password;
    protected Role role;
    protected String city;
    protected String neighborhood;
    protected LocalDateTime createdAt;
    protected LocalDateTime updatedAt;
    protected boolean active;
    protected boolean emailVerified;
    protected boolean phoneVerified;
    protected String profilePhotoUrl;
    

    // Constructeur protégé
    public User() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.active = true;
        this.emailVerified = false;
        this.phoneVerified = false;
        this.profilePhotoUrl = null;
        
    }

    // ========== Business Logic Methods ==========
    public void updateProfile(String firstName, String lastName, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.updatedAt = LocalDateTime.now();
    }

    public void verifyEmail() {
        this.emailVerified = true;
        this.updatedAt = LocalDateTime.now();
    }

    public void verifyPhone() {
        this.phoneVerified = true;
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        this.active = true;
        this.updatedAt = LocalDateTime.now();
    }

    public void deactivate() {
        this.active = false;
        this.updatedAt = LocalDateTime.now();
    }

    public void changePassword(String newPassword) {
        this.password = newPassword;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isClient() {
        return this.role == Role.CLIENT;
    }

    // ========== Profile ==========
    public void updateProfilePhoto(String photoUrl) {
        this.profilePhotoUrl = photoUrl;
        this.updatedAt = LocalDateTime.now();
    }

    public void deleteProfilePhoto() {
        this.profilePhotoUrl = null;
        this.updatedAt = LocalDateTime.now();
    }

    public String getProfilePhotoUrl() {
        return profilePhotoUrl;
    }

    public void setProfilePhotoUrl(String profilePhotoUrl) {
        this.profilePhotoUrl = profilePhotoUrl;
    }

    public boolean isTechnician() {
        return this.role == Role.TECHNICIAN;
    }

    public boolean isAdmin() {
        return this.role == Role.ADMIN;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    // ========== Getters ==========
    public Long getIdUser() {
        return idUser;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public boolean isActive() {
        return active;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public boolean isPhoneVerified() {
        return phoneVerified;
    }

    // ========== Setters (pour reconstruction depuis DB) ==========
    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    // ========== equals & hashCode ==========
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(idUser, user.idUser) && 
               Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idUser, email);
    }

    @Override
    public String toString() {
        return "User{" +
                "idUser=" + idUser +
                ", fullName='" + getFullName() + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                ", active=" + active +
                '}';
    }

    // Méthode de restauration pour le mapping depuis l'entité
    public void restore(Long idUser, String firstName, String lastName,
                        String email, String phone, String password,
                        Role role, String city, String neighborhood, LocalDateTime createdAt,
                        LocalDateTime updatedAt, boolean active,
                        boolean emailVerified, boolean phoneVerified,
                        String profilePhotoUrl) {
        this.idUser = idUser;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.role = role;
        this.city = city;
        this.neighborhood = neighborhood;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.active = active;
        this.emailVerified = emailVerified;
        this.phoneVerified = phoneVerified;
        this.profilePhotoUrl = profilePhotoUrl;
    }

    public String getCity() {
        return city;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public void setPhoneVerified(boolean phoneVerified) {
        this.phoneVerified = phoneVerified;
    }
}