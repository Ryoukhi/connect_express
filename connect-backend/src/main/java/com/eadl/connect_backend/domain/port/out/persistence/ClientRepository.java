package com.eadl.connect_backend.domain.port.out.persistence;

import com.eadl.connect_backend.domain.model.user.Client;
import java.util.List;
import java.util.Optional;

/**
 * Port OUT - Repository Client
 */
public interface ClientRepository {
    
    /**
     * Sauvegarde un client
     */
    Client save(Client client);
    
    /**
     * Récupère un client par son ID utilisateur
     */
    Optional<Client> findByUserId(Long idUser);
    
    /**
     * Récupère tous les clients
     */
    List<Client> findAll();
    
    /**
     * Recherche des clients par adresse
     */
    List<Client> findByAddressContaining(String address);
    
    /**
     * Compte le nombre de clients
     */
    Long count();
    
    /**
     * Supprime un client
     */
    void delete(Client client);
}