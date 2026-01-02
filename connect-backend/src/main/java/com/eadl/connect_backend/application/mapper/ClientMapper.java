package com.eadl.connect_backend.application.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.eadl.connect_backend.application.dto.ClientDto;
import com.eadl.connect_backend.domain.model.user.Client;

public class ClientMapper {

    public ClientDto toDto(Client client) {
        if (client == null) return null;

        ClientDto dto = new ClientDto();
        dto.setFirstName(client.getFirstName());
        dto.setLastName(client.getLastName());
        dto.setEmail(client.getEmail());
        dto.setPassword(client.getPassword());
        dto.setPhone(client.getPhone());
        dto.setCity(client.getCity());
        dto.setNeighborhood(client.getNeighborhood());
        dto.setProfilePhotoUrl(client.getProfilePhotoUrl());

        return dto;
    }

    public Client toModel(ClientDto dto) {
        if (dto == null) return null;

        Client client = new Client();
        client.setFirstName(dto.getFirstName());
        client.setLastName(dto.getLastName());
        client.setEmail(dto.getEmail());
        client.setPassword(dto.getPassword());
        client.setPhone(dto.getPhone());
        client.setCity(dto.getCity());
        client.setNeighborhood(dto.getNeighborhood());
        client.setProfilePhotoUrl(dto.getProfilePhotoUrl());

        return client;
    }

    public List<ClientDto> toDtoList(List<Client> clients) {
        return clients.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<Client> toModelList(List<ClientDto> dtos) {
        return dtos.stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }

}
