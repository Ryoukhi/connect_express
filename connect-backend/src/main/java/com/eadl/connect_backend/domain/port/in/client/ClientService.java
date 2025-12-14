package com.eadl.connect_backend.domain.port.in.client;

import com.eadl.connect_backend.domain.model.Client;
import java.util.List;
import java.util.Optional;

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
    Client updateClientContact(Long id, String email, String phone);

    /**
     * Met à jour l'adresse d'un client
     * @param id L'identifiant du client
     * @param address La nouvelle adresse
     * @param city La nouvelle ville
     * @param postalCode Le nouveau code postal
     * @return Le client avec l'adresse mise à jour
     */
    Client updateClientAddress(Long id, String address, String city, String postalCode);

    // ========== DELETE ==========

    /**
     * Supprime définitivement un client de la base de données
     * @param id L'identifiant du client à supprimer
     * @throws EntityNotFoundException si le client n'existe pas
     */
    void deleteClient(Long id);

    /**
     * Désactive un client sans le supprimer physiquement (soft delete)
     * Le client reste dans la base mais est marqué comme inactif
     * @param id L'identifiant du client à désactiver
     */
    void softDeleteClient(Long id);

    /**
     * Réactive un client précédemment désactivé
     * @param id L'identifiant du client à réactiver
     */
    void reactivateClient(Long id);

    // ========== MÉTHODES UTILITAIRES ==========

    /**
     * Vérifie si un email est déjà utilisé par un client
     * @param email L'adresse email à vérifier
     * @return true si l'email existe déjà, false sinon
     */
    boolean existsByEmail(String email);

    /**
     * Vérifie si un numéro de téléphone est déjà utilisé
     * @param phone Le numéro de téléphone à vérifier
     * @return true si le téléphone existe déjà, false sinon
     */
    boolean existsByPhone(String phone);

    /**
     * Compte le nombre total de clients dans le système
     * @return Le nombre de clients
     */
    long countClients();

    /**
     * Compte le nombre de clients actifs
     * @return Le nombre de clients actifs
     */
    long countActiveClients();

    /**
     * Recherche des clients par mot-clé
     * La recherche peut porter sur le nom, prénom, email, téléphone, etc.
     * @param keyword Le mot-clé de recherche
     * @return Liste des clients correspondant à la recherche
     */
    List<Client> searchClients(String keyword);

    /**
     * Récupère l'historique des réservations d'un client
     * @param clientId L'identifiant du client
     * @return Liste des réservations du client
     */
    List<Object> getClientReservations(Long clientId);
}