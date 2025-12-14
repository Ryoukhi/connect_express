package com.eadl.connect_backend.application.service.client;

import com.eadl.connect_backend.domain.model.Client;
import com.eadl.connect_backend.domain.port.in.client.ClientService;
import com.eadl.connect_backend.domain.port.out.ClientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implémentation du service de gestion des clients
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    // ========== CREATE ==========

    @Override
    public Client createClient(Client client) {
        log.info("Création d'un nouveau client avec l'email: {}", client.getEmail());

        if (client.getEmail() != null && existsByEmail(client.getEmail())) {
            throw new IllegalArgumentException("Un client avec cet email existe déjà");
        }

        if (client.getPhone() != null && existsByPhone(client.getPhone())) {
            throw new IllegalArgumentException("Un client avec ce numéro de téléphone existe déjà");
        }

        if (client.getActive() == null) {
            client.setActive(true);
        }

        Client savedClient = clientRepository.save(client);
        log.info("Client créé avec succès avec l'ID: {}", savedClient.getId());
        return savedClient;
    }

    // ========== READ ==========

    @Override
    @Transactional(readOnly = true)
    public Optional<Client> getClientById(Long id) {
        log.debug("Recherche du client avec l'ID: {}", id);
        return clientRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Client> getClientByEmail(String email) {
        log.debug("Recherche du client avec l'email: {}", email);
        return clientRepository.findByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Client> getClientByPhone(String phone) {
        log.debug("Recherche du client avec le téléphone: {}", phone);
        return clientRepository.findByPhone(phone);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Client> getAllClients() {
        log.debug("Récupération de tous les clients");
        return clientRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Client> getActiveClients() {
        // TODO: Implémenter
        return List.of();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Client> getClientsByCity(String city) {
        // TODO: Implémenter
        return List.of();
    }

    // ========== UPDATE ==========

    @Override
    public Client updateClient(Long id, Client client) {
        log.info("Mise à jour du client avec l'ID: {}", id);

        Client existingClient = clientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Client non trouvé avec l'ID: " + id));

        if (client.getFirstName() != null) {
            existingClient.setFirstName(client.getFirstName());
        }
        if (client.getLastName() != null) {
            existingClient.setLastName(client.getLastName());
        }
        if (client.getEmail() != null && !client.getEmail().equals(existingClient.getEmail())) {
            if (existsByEmail(client.getEmail())) {
                throw new IllegalArgumentException("Cet email est déjà utilisé");
            }
            existingClient.setEmail(client.getEmail());
        }
        if (client.getPhone() != null && !client.getPhone().equals(existingClient.getPhone())) {
            if (existsByPhone(client.getPhone())) {
                throw new IllegalArgumentException("Ce numéro de téléphone est déjà utilisé");
            }
            existingClient.setPhone(client.getPhone());
        }
        if (client.getAddress() != null) {
            existingClient.setAddress(client.getAddress());
        }
        if (client.getCity() != null) {
            existingClient.setCity(client.getCity());
        }
        if (client.getPostalCode() != null) {
            existingClient.setPostalCode(client.getPostalCode());
        }

        Client updatedClient = clientRepository.save(existingClient);
        log.info("Client mis à jour avec succès: {}", id);
        return updatedClient;
    }

    @Override
    public Client updateClientContact(Long id, String email, String phone) {
        // TODO: Implémenter
        return null;
    }

    @Override
    public Client updateClientAddress(Long id, String address, String city, String postalCode) {
        // TODO: Implémenter
        return null;
    }

    // ========== DELETE ==========

    @Override
    public void deleteClient(Long id) {
        log.info("Suppression du client avec l'ID: {}", id);

        if (!clientRepository.existsById(id)) {
            throw new IllegalArgumentException("Client non trouvé avec l'ID: " + id);
        }

        clientRepository.deleteById(id);
        log.info("Client supprimé avec succès: {}", id);
    }

    @Override
    public void softDeleteClient(Long id) {
        // TODO: Implémenter
    }

    @Override
    public void reactivateClient(Long id) {
        // TODO: Implémenter
    }

    // ========== MÉTHODES UTILITAIRES ==========

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return clientRepository.existsByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByPhone(String phone) {
        return clientRepository.existsByPhone(phone);
    }

    @Override
    @Transactional(readOnly = true)
    public long countClients() {
        return clientRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public long countActiveClients() {
        // TODO: Implémenter
        return 0;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Client> searchClients(String keyword) {
        // TODO: Implémenter
        return List.of();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object> getClientReservations(Long clientId) {
        // TODO: Implémenter
        return List.of();
    }
}