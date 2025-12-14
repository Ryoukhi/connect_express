package com.eadl.connect_backend.domain.port.in.client;

import java.util.List;
import java.util.Optional;

import com.eadl.connect_backend.domain.model.user.Client;

/**
 * Interface de service pour la gestion des clients
 * Définit les opérations CRUD et les fonctionnalités métier liées aux clients
 */
public interface ClientService {

    // ========== CREATE ==========

    /**
     * Crée un nouveau client dans le système
     * @param client L'objet client à créer
     * @return Le client créé avec son ID généré
     * @throws IllegalArgumentException si les données sont invalides
     */
    Client createClient(Client client);

    // ========== READ ==========

    /**
     * Récupère un client par son identifiant
     * @param id L'identifiant du client
     * @return Optional contenant le client si trouvé, sinon Optional vide
     */
    Optional<Client> getClientById(Long id);

    /**
     * Récupère un client par son email
     * @param email L'adresse email du client
     * @return Optional contenant le client si trouvé, sinon Optional vide
     */
    Optional<Client> getClientByEmail(String email);

    /**
     * Récupère un client par son numéro de téléphone
     * @param phone Le numéro de téléphone du client
     * @return Optional contenant le client si trouvé, sinon Optional vide
     */
    Optional<Client> getClientByPhone(String phone);

    /**
     * Récupère la liste complète de tous les clients
     * @return Liste de tous les clients enregistrés
     */
    List<Client> getAllClients();

    /**
     * Récupère tous les clients actifs (non désactivés)
     * @return Liste des clients actifs
     */
    List<Client> getActiveClients();

    /**
     * Récupère les clients par ville
     * @param city La ville à filtrer
     * @return Liste des clients de la ville spécifiée
     */
    List<Client> getClientsByCity(String city);

    // ========== UPDATE ==========

    /**
     * Met à jour complètement un client
     * @param id L'identifiant du client à modifier
     * @param client L'objet client avec les nouvelles données
     * @return Le client mis à jour
     * @throws EntityNotFoundException si le client n'existe pas
     */
    Client updateClient(Long id, Client client);

    /**
     * Met à jour les informations de contact d'un client
     * @param id L'identifiant du client
     * @param email Le nouvel email
     * @param phone Le nouveau téléphone
     * @return Le client avec les contacts mis à jour
     */
    void deleteClient(Long id);

    // ========== MÉTHODES UTILITAIRES ==========

    /**
     * Récupère l'historique des réservations d'un client
     * @param clientId L'identifiant du client
     * @return Liste des réservations du client
     */
    List<Object> getClientReservations(Long clientId);
}