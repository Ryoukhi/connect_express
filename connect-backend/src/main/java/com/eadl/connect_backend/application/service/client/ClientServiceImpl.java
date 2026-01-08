package com.eadl.connect_backend.application.service.client;

import com.eadl.connect_backend.domain.model.reservation.Reservation;
import com.eadl.connect_backend.domain.model.user.Client;
import com.eadl.connect_backend.domain.port.in.client.ClientService;
import com.eadl.connect_backend.domain.port.out.persistence.ClientRepository;
import com.eadl.connect_backend.domain.port.out.persistence.ReservationRepository;
import com.eadl.connect_backend.domain.port.out.security.CurrentUserProvider;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@Slf4j
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final ReservationRepository reservationRepository;
    private final CurrentUserProvider currentUserProvider;

    public ClientServiceImpl(ClientRepository clientRepository,
                             ReservationRepository reservationRepository,
                             CurrentUserProvider currentUserProvider) {
        this.clientRepository = clientRepository;
        this.reservationRepository = reservationRepository;
        this.currentUserProvider = currentUserProvider;
    }

    @Override
    public Client registerClient(Client client) {
        log.info("Enregistrement d'un nouveau client: {} {}", client.getFirstName(), client.getLastName());

        client.activate();
        // On peut éventuellement initialiser d'autres champs ici

        Client saved = clientRepository.save(client);
        log.info("Client enregistré avec succès id={}", saved.getId());
        return saved;
    }

    @Override
    public List<Client> getActiveClients() {
        log.debug("Récupération des clients actifs");
        List<Client> clients = clientRepository.findByActive(true);
        log.debug("Nombre de clients actifs récupérés: {}", clients.size());
        return clients;
    }

    @Override
    public List<Client> getClientsByCity(String city) {
        log.debug("Récupération des clients dans la ville: '{}'", city);

        if (!StringUtils.hasText(city)) {
            log.error("Nom de ville vide fourni pour la recherche des clients");
            throw new IllegalArgumentException("City must not be empty");
        }

        List<Client> clients = clientRepository.findByCity(city);
        log.debug("Nombre de clients récupérés pour la ville '{}': {}", city, clients.size());
        return clients;
    }

    @Override
    public List<Reservation> getClientReservations() {
        Long clientId = currentUserProvider.getCurrentUserId();
        log.debug("Récupération des réservations pour le client id={}", clientId);

        List<Reservation> reservations = reservationRepository.findByClientId(clientId);
        log.debug("Nombre de réservations récupérées pour le client id={}: {}", clientId, reservations.size());
        return reservations;
    }

    @Override
    public Long countActiveClients() {
        log.debug("Comptage des clients actifs");
        Long count = clientRepository.countByActive(true);
        log.debug("Nombre de clients actifs: {}", count);
        return count;
    }
}
