package com.eadl.connect_backend.domain.port.in.client;

import java.util.List;

import com.eadl.connect_backend.domain.model.reservation.Reservation;
import com.eadl.connect_backend.domain.model.user.Client;

/**
 * Interface de service pour la gestion des clients
 * Définit les opérations CRUD et les fonctionnalités métier liées aux clients
 */
public interface ClientService {

    Client registerClient(Client client);

    List<Client> getActiveClients();

    List<Client> getClientsByCity(String city);

    List<Reservation> getClientReservations();

    Long countActiveClients();


}

