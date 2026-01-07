package com.eadl.connect_backend.infrastructure.adapter.out.persistence.adapter;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.eadl.connect_backend.domain.model.user.Client;
import com.eadl.connect_backend.domain.port.out.persistence.ClientRepository;
import com.eadl.connect_backend.infrastructure.adapter.out.persistence.entity.UserEntity;
import com.eadl.connect_backend.infrastructure.adapter.out.persistence.mapper.ClientEntityMapper;
import com.eadl.connect_backend.infrastructure.adapter.out.persistence.repository.jpa.ClientJpaRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ClientRepositoryAdapter implements ClientRepository {

    private static final Logger log = LoggerFactory.getLogger(ClientRepositoryAdapter.class);

    private final ClientJpaRepository clientJpaRepository;
    private final ClientEntityMapper clientEntityMapper;

    @Override
    public Client save(Client client) {
        log.info("Saving client: {}", client);

        UserEntity entity = clientEntityMapper.toEntity(client);
        UserEntity saved = clientJpaRepository.save(entity);
        Client savedClient = clientEntityMapper.toDomain(saved);

        log.info("Saved client: {}", savedClient);
        return savedClient;
    }

    @Override
    public Optional<Client> findByidUser(Long idUser) {
        log.info("Finding client by user id: {}", idUser);

        Optional<Client> client = clientJpaRepository.findByidUser(idUser)
                .map(clientEntityMapper::toDomain);

        client.ifPresentOrElse(
                c -> log.info("Found client: {}", c),
                () -> log.warn("No client found with user id: {}", idUser)
        );

        return client;
    }

    @Override
    public List<Client> findAll() {
        log.info("Fetching all clients");

        List<Client> clients = clientJpaRepository.findAll()
                .stream()
                .map(clientEntityMapper::toDomain)
                .toList();

        log.info("Found {} clients", clients.size());
        return clients;
    }

    @Override
    public Long count() {
        log.info("Counting all clients");

        Long total = clientJpaRepository.count();
        log.info("Total clients: {}", total);

        return total;
    }

    @Override
    public void delete(Client client) {
        log.info("Deleting client: {}", client);

        UserEntity entity = clientEntityMapper.toEntity(client);
        clientJpaRepository.delete(entity);

        log.info("Client deleted: {}", client);
    }

    @Override
    public List<Client> findByActive(boolean active) {
        log.info("Fetching clients by active = {}", active);

        List<Client> clients = clientJpaRepository.findByActive(active)
                .stream()
                .map(clientEntityMapper::toDomain)
                .toList();

        log.info("Found {} clients with active = {}", clients.size(), active);
        return clients;
    }

    @Override
    public List<Client> findByCity(String city) {
        log.info("Fetching clients by city: {}", city);

        List<Client> clients = clientJpaRepository.findByCityIgnoreCase(city)
                .stream()
                .map(clientEntityMapper::toDomain)
                .toList();

        log.info("Found {} clients in city {}", clients.size(), city);
        return clients;
    }

    @Override
    public Optional<Client> findById(Long clientId) {
        log.info("Finding client by id: {}", clientId);

        Optional<Client> client = clientJpaRepository.findById(clientId)
                .map(clientEntityMapper::toDomain);

        client.ifPresentOrElse(
                c -> log.info("Found client: {}", c),
                () -> log.warn("No client found with id: {}", clientId)
        );

        return client;
    }

    @Override
    public Long countByActive(boolean active) {
        log.info("Counting clients with active = {}", active);

        Long total = clientJpaRepository.countByActive(active);
        log.info("Total clients with active = {}: {}", active, total);

        return total;
    }
}
