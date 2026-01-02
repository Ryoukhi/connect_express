package com.eadl.connect_backend.application.mapper;

import org.springframework.stereotype.Component;

import com.eadl.connect_backend.application.dto.RegisterAdminDto;
import com.eadl.connect_backend.domain.model.user.Admin;
import com.eadl.connect_backend.domain.model.user.User;

@Component
public class RegisterAdminMapper {

    public Admin toModel(RegisterAdminDto dto) {
        Admin admin = new Admin();
        admin.setFirstName(dto.getFirstName());
        admin.setLastName(dto.getLastName());
        admin.setEmail(dto.getEmail());
        admin.setPassword(dto.getPassword());
        admin.setPhone(dto.getPhone());
        admin.setCity(dto.getCity());
        admin.setNeighborhood(dto.getNeighborhood());
        admin.setProfilePhotoUrl(dto.getProfilePhotoUrl());
        return admin;
    }

    public RegisterAdminDto toDto(User user) {
        RegisterAdminDto dto = new RegisterAdminDto();
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