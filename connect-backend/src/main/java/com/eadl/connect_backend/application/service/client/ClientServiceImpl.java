package com.eadl.connect_backend.application.service.client;

import com.eadl.connect_backend.domain.model.reservation.Reservation;
import com.eadl.connect_backend.domain.model.user.Client;
import com.eadl.connect_backend.domain.port.in.client.ClientService;
import com.eadl.connect_backend.domain.port.out.persistence.ClientRepository;
import com.eadl.connect_backend.domain.port.out.persistence.ReservationRepository;
import com.eadl.connect_backend.domain.port.out.security.CurrentUserProvider;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
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
        // Par défaut un client est actif
        client.activate();
        // On peut éventuellement initialiser d'autres champs ici
        return clientRepository.save(client);
    }

    @Override
    public List<Client> getActiveClients() {
        return clientRepository.findByActive(true);
    }

    @Override
    public List<Client> getClientsByCity(String city) {
        if (!StringUtils.hasText(city)) {
            throw new IllegalArgumentException("City must not be empty");
        }
        return clientRepository.findByCity(city);
    }

    @Override
    public List<Reservation> getClientReservations() {

        Long clientId = currentUserProvider.getCurrentUserId();
        
        return reservationRepository.findByClientId(clientId);
    }

    @Override
    public Long countActiveClients() {
        return clientRepository.countByActive(true);
    }
}