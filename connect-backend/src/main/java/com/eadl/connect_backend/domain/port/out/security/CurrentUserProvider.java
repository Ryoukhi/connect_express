package com.eadl.connect_backend.domain.port.out.security;

import com.eadl.connect_backend.domain.model.user.User;

public interface CurrentUserProvider {

    /**
     * 
     * @return l'ID de l'utilisateur actuellement connecté
     */

    Long getCurrentUserId();

    /**
     * Retourne l’utilisateur connecté
     * @throws IllegalStateException si aucun utilisateur n’est authentifié
     */
    User getCurrentUser();

    /**
     * Indique si un utilisateur est authentifié
     */
    boolean isAuthenticated();
}
