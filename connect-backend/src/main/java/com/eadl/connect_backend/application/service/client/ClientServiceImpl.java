package com.eadl.connect_backend.application.service.client;

import com.eadl.connect_backend.domain.model.reservation.Reservation;
import com.eadl.connect_backend.domain.model.user.Client;
import com.eadl.connect_backend.domain.port.in.client.ClientService;
import com.eadl.connect_backend.domain.port.out.persistence.ClientRepository;
import com.eadl.connect_backend.domain.port.out.persistence.ReservationRepository;
import com.eadl.connect_backend.domain.port.out.persistence.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implémentation du service de gestion des clients
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;

    @Override
    public Optional<Client> getClientById(Long id) {
        return clientRepository.findByUserId(id);
    }

    @Override
    public Optional<Client> getClientByEmail(String email) {
        return userRepository.findByEmail(email)
                .filter(u -> u instanceof Client)
                .map(u -> (Client) u);
    }

    @Override
    public Optional<Client> getClientByPhone(String phone) {
        return userRepository.findByPhone(phone)
                .filter(u -> u instanceof Client)
                .map(u -> (Client) u);
    }

    @Override
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    @Override
    public List<Client> getActiveClients() {
        return clientRepository.findAll()
                .stream()
                .filter(Client::isActive)
                .collect(Collectors.toList());
    }

    @Override
    public List<Client> getClientsByCity(String city) {
        if (city == null || city.trim().isEmpty()) {
            return getAllClients();
        }
        return clientRepository.findByAddressContaining(city);
    }

    @Override
    public Client updateClient(Long id, Client client) {
        log.info("Mise à jour du client id={}", id);
        Client existing = clientRepository.findByUserId(id)
                .orElseThrow(() -> new EntityNotFoundException("Client introuvable"));

        existing.updateProfile(client.getFirstName() == null ? existing.getFirstName() : client.getFirstName(),
                client.getLastName() == null ? existing.getLastName() : client.getLastName(),
                client.getPhone() == null ? existing.getPhone() : client.getPhone());

        if (client.getAddress() != null) {
            existing.updateAddress(client.getAddress());
        }

        return clientRepository.save(existing);
    }

    @Override
    public void deleteClient(Long id) {
        log.info("Suppression du client id={}", id);
        Client existing = clientRepository.findByUserId(id)
                .orElseThrow(() -> new EntityNotFoundException("Client introuvable"));

        // Optional: ensure reservations are handled before deletion (soft delete/move)
        reservationRepository.findByClientId(id).forEach(res -> {
            // If reservation is active or pending, block deletion
            if (res.isActive() || res.isPending()) {
                throw new IllegalStateException("Impossible de supprimer le client: réservations actives ou en attente existantes");
            }
        });

        clientRepository.delete(existing);
        log.info("Client supprimé id={}", id);
    }

    @Override
    public List<Object> getClientReservations(Long clientId) {
        List<Reservation> reservations = reservationRepository.findByClientId(clientId);
        return reservations.stream().map(r -> (Object) r).collect(Collectors.toList());
    }

}
