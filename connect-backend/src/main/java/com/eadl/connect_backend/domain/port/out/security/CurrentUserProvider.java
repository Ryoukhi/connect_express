package com.eadl.connect_backend.domain.port.out.security;

public interface CurrentUserProvider {

    /**
     * 
     * @return l'ID de l'utilisateur actuellement connecté
     */

    Long getCurrentUserId();
}
