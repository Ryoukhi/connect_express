package com.eadl.connect_backend.domain.port.in.user;

import com.eadl.connect_backend.domain.model.User;

/**
 * Interface de service pour l'authentification des utilisateurs
 * Gère les opérations de connexion et d'inscription
 */
public interface AuthService {

    /**
     * Authentifie un utilisateur avec son email et mot de passe
     * @param email L'adresse email de l'utilisateur
     * @param password Le mot de passe en clair
     * @return L'utilisateur authentifié avec un token de session
     * @throws AuthenticationException si les identifiants sont incorrects
     * @throws AccountDisabledException si le compte est désactivé
     */
    User login(String email, String password);

    /**
     * Enregistre un nouvel utilisateur dans le système
     * Crée le compte, hash le mot de passe et assigne un rôle par défaut
     * @param user L'objet utilisateur contenant les informations d'inscription
     * @return L'utilisateur créé avec son ID et token de session
     * @throws EmailAlreadyExistsException si l'email est déjà utilisé
     * @throws ValidationException si les données sont invalides
     */
    User register(User user);

}