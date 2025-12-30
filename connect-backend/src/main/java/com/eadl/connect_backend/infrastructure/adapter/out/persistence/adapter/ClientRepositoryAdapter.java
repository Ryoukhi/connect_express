package com.eadl.connect_backend.infrastructure.adapter.out.persistence.adapter;

import java.util.List;
import java.util.Optional;

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

    private final ClientJpaRepository clientJpaRepository;
    private final ClientEntityMapper clientEntityMapper;

    @Override
    public Client save(Client client) {
        UserEntity entity = clientEntityMapper.toEntity(client);
        UserEntity saved = clientJpaRepository.save(entity);
        return clientEntityMapper.toDomain(saved);
    }

    @Override
    public Optional<Client> findByidUser(Long idUser) {
        return clientJpaRepository.findByidUser(idUser)
                .map(clientEntityMapper::toDomain);
    }

    @Override
    public List<Client> findAll() {
        return clientJpaRepository.findAll()
                .stream()
                .map(clientEntityMapper::toDomain)
                .toList();
    }

    @Override
    public Long count() {
        return clientJpaRepository.count();
    }

    @Override
    public void delete(Client client) {
        UserEntity entity = clientEntityMapper.toEntity(client);
        clientJpaRepository.delete(entity);
    }

    @Override
    public List<Client> findByActive(boolean active) {
        return clientJpaRepository.findByActive(active)
                .stream()
                .map(clientEntityMapper::toDomain)
                .toList();
    }

    @Override
    public List<Client> findByCity(String city) {
        return clientJpaRepository.findByCityIgnoreCase(city)
                .stream()
                .map(clientEntityMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Client> findById(Long clientId) {
        return clientJpaRepository.findById(clientId)
                .map(clientEntityMapper::toDomain);
    }

    @Override
    public Long countByActive(boolean active) {
        return clientJpaRepository.countByActive(active);
    }
}
