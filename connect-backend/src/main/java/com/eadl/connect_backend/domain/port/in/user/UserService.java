package com.eadl.connect_backend.domain.port.in.user;

import com.eadl.connect_backend.domain.model.User;
import java.util.List;
import java.util.Optional;

/**
 * Interface de service pour la gestion des utilisateurs
 * Définit les opérations CRUD et les fonctionnalités métier liées aux utilisateurs
 */
public interface UserService {

    // ========== CREATE ==========

    /**
     * Crée un nouvel utilisateur dans le système
     * @param user L'objet utilisateur à créer
     * @return L'utilisateur créé avec son ID généré
     * @throws IllegalArgumentException si l'email existe déjà
     */
    User createUser(User user);

    // ========== READ ==========

    /**
     * Récupère un utilisateur par son identifiant
     * @param id L'identifiant de l'utilisateur
     * @return Optional contenant l'utilisateur si trouvé, sinon Optional vide
     */
    Optional<User> getUserById(Long id);

    /**
     * Récupère un utilisateur par son adresse email
     * @param email L'adresse email de l'utilisateur
     * @return Optional contenant l'utilisateur si trouvé, sinon Optional vide
     */
    Optional<User> getUserByEmail(String email);

    /**
     * Récupère la liste complète de tous les utilisateurs
     * @return Liste de tous les utilisateurs enregistrés
     */
    List<User> getAllUsers();

    /**
     * Récupère tous les utilisateurs ayant un rôle spécifique
     * @param role Le rôle à filtrer (ex: "ADMIN", "USER", "TECHNICIAN")
     * @return Liste des utilisateurs correspondant au rôle
     */
    List<User> getUsersByRole(String role);

    // ========== UPDATE ==========

    /**
     * Met à jour complètement un utilisateur
     * @param id L'identifiant de l'utilisateur à modifier
     * @param user L'objet utilisateur avec les nouvelles données
     * @return L'utilisateur mis à jour
     * @throws EntityNotFoundException si l'utilisateur n'existe pas
     */
    User updateUser(Long id, User user);

    /**
     * Met à jour uniquement les informations de profil de l'utilisateur
     * (nom, prénom, téléphone, etc.) sans toucher aux données sensibles
     * @param id L'identifiant de l'utilisateur
     * @param user L'objet contenant les données de profil à mettre à jour
     * @return L'utilisateur avec le profil mis à jour
     */
    User updateUserProfile(Long id, User user);

    /**
     * Met à jour le mot de passe d'un utilisateur
     * @param id L'identifiant de l'utilisateur
     * @param oldPassword L'ancien mot de passe pour vérification
     * @param newPassword Le nouveau mot de passe
     * @return L'utilisateur avec le mot de passe mis à jour
     * @throws IllegalArgumentException si l'ancien mot de passe est incorrect
     */
    User updateUserPassword(Long id, String oldPassword, String newPassword);

    // ========== DELETE ==========

    /**
     * Supprime définitivement un utilisateur de la base de données
     * @param id L'identifiant de l'utilisateur à supprimer
     * @throws EntityNotFoundException si l'utilisateur n'existe pas
     */
    void deleteUser(Long id);

    /**
     * Désactive un utilisateur sans le supprimer physiquement (soft delete)
     * L'utilisateur reste dans la base mais est marqué comme inactif
     * @param id L'identifiant de l'utilisateur à désactiver
     */
    void softDeleteUser(Long id);

    // ========== MÉTHODES UTILITAIRES ==========

    /**
     * Vérifie si un email est déjà utilisé dans le système
     * @param email L'adresse email à vérifier
     * @return true si l'email existe déjà, false sinon
     */
    boolean existsByEmail(String email);

    /**
     * Compte le nombre total d'utilisateurs dans le système
     * @return Le nombre d'utilisateurs
     */
    long countUsers();

    /**
     * Recherche des utilisateurs par mot-clé
     * La recherche peut porter sur le nom, prénom, email, etc.
     * @param keyword Le mot-clé de recherche
     * @return Liste des utilisateurs correspondant à la recherche
     */
    List<User> searchUsers(String keyword);
}