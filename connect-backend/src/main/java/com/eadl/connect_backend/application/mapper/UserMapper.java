package com.eadl.connect_backend.application.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.eadl.connect_backend.application.dto.UserDto;
import com.eadl.connect_backend.domain.model.user.User;

/**
 * Mapper utilitaire pour convertir les entités User (et spécialisations)
 * en DTOs de réponse.
 */
@Component
public class UserMapper {

    private UserMapper() {
        // utilitaire
    }

    public static UserDto toDto(User user) {
        if (user == null) return null;

        UserDto dto = new UserDto();
        dto.setIdUser(user.getIdUser());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setRole(user.getRole());
        dto.setCity(user.getCity());
        dto.setNeighborhood(user.getNeighborhood());

        return dto;
    }


    public static List<UserDto> toDtos(List<User> users) {
        if (users == null) return List.of();

        return users.stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }

    
}