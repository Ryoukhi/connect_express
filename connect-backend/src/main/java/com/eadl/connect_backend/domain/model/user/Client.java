package com.eadl.connect_backend.domain.model.user;

/**
 * Client - Spécialisation de User avec héritage
 */
public class Client extends User {
    private String address;

    private Client() {
        super();
        this.role = Role.CLIENT;
    }

    // ========== Factory Method ==========
    public static Client create(String firstName, String lastName, 
                               String email, String phone, String password, 
                               String address) {
        Client client = new Client();
        client.firstName = firstName;
        client.lastName = lastName;
        client.email = email;
        client.phone = phone;
        client.password = password;
        client.address = address;
        return client;
    }

    // ========== Business Logic ==========
    public void updateAddress(String newAddress) {
        this.address = newAddress;
        this.updatedAt = java.time.LocalDateTime.now();
    }

    // ========== Getters & Setters ==========
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Client{" +
                "idUser=" + idUser +
                ", fullName='" + getFullName() + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", active=" + active +
                '}';
    }
}