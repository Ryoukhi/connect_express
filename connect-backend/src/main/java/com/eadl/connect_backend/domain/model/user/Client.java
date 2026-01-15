package com.eadl.connect_backend.domain.model.user;

/**
 * Client - Spécialisation de User avec héritage
 */
public class Client extends User {

    public Client() {
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
        return client;
    }

    
    // ========== Getters & Setters ==========
    

    

    @Override
    public String toString() {
        return "Client{" +
                "idUser=" + idUser +
                ", fullName='" + getFullName() + '\'' +
                ", email='" + email + '\'' +
                ", active=" + active +
                '}';
    }

    public void setId(long id) {
    }
}