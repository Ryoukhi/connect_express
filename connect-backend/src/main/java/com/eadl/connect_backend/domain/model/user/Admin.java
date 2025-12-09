package com.eadl.connect_backend.domain.model.user;

/**
 * Admin - Spécialisation de User avec héritage
 */
public class Admin extends User {

    private Admin() {
        super();
        this.role = Role.ADMIN;
        this.emailVerified = true;
        this.phoneVerified = true;
    }

    // ========== Factory Method ==========
    public static Admin create(String firstName, String lastName, 
                              String email, String phone, String password) {
        Admin admin = new Admin();
        admin.firstName = firstName;
        admin.lastName = lastName;
        admin.email = email;
        admin.phone = phone;
        admin.password = password;
        return admin;
    }

    // ========== Business Logic spécifique Admin ==========
    public void suspendUser(User user, String reason) {
        user.deactivate();
        // Log de l'action dans AdminAction (sera géré par le service)
    }

    public void reactivateUser(User user) {
        user.activate();
        // Log de l'action dans AdminAction (sera géré par le service)
    }

    @Override
    public String toString() {
        return "Admin{" +
                "idUser=" + idUser +
                ", fullName='" + getFullName() + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}