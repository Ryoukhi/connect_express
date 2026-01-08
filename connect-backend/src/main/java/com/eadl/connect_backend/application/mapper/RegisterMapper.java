package com.eadl.connect_backend.application.mapper;

import org.springframework.stereotype.Component;

import com.eadl.connect_backend.application.dto.RegisterDto;
import com.eadl.connect_backend.application.dto.RegisterResponseDto;
import com.eadl.connect_backend.domain.model.user.Client;
import com.eadl.connect_backend.domain.model.user.Technician;
import com.eadl.connect_backend.domain.model.user.User;

@Component
public class RegisterMapper {

    public Client toModel(RegisterDto dto) {
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

    public Technician toModelTechnician(RegisterDto dto) {
        Technician technician = new Technician();
        technician.setFirstName(dto.getFirstName());
        technician.setLastName(dto.getLastName());
        technician.setEmail(dto.getEmail());
        technician.setPassword(dto.getPassword());
        technician.setPhone(dto.getPhone());
        technician.setCity(dto.getCity());
        technician.setNeighborhood(dto.getNeighborhood());
        technician.setProfilePhotoUrl(dto.getProfilePhotoUrl());
        return technician;
    }

    public RegisterResponseDto toDto(User user) {
        RegisterResponseDto dto = new RegisterResponseDto();
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setCity(user.getCity());
        dto.setNeighborhood(user.getNeighborhood());
        dto.setProfilePhotoUrl(user.getProfilePhotoUrl());
        return dto;
    }
}