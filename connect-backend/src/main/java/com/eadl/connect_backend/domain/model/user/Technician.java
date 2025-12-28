package com.eadl.connect_backend.domain.model.user;

/**
 * Technician - Spécialisation de User avec héritage
 */
public class Technician extends User {

    public Technician() {
        super();
        this.role = Role.TECHNICIAN;
        this.active = false; // Inactif jusqu'à validation KYC
    }

    // ========== Factory Method ==========
    public static Technician create(String firstName, String lastName, 
                                   String email, String phone, String password) {
        Technician technician = new Technician();
        technician.firstName = firstName;
        technician.lastName = lastName;
        technician.email = email;
        technician.phone = phone;
        technician.password = password;
        return technician;
    }

    // ========== Business Logic spécifique ==========
    public void approveKyc() {
        this.activate();
        this.updatedAt = java.time.LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Technician{" +
                "idUser=" + idUser +
                ", fullName='" + getFullName() + '\'' +
                ", email='" + email + '\'' +
                ", verified=" + active +
                '}';
    }
}