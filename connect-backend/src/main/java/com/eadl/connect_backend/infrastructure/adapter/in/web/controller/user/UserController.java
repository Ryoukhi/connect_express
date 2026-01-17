package com.eadl.connect_backend.infrastructure.adapter.in.web.controller.user;

import com.eadl.connect_backend.application.dto.UserDto;
import com.eadl.connect_backend.application.mapper.UserMapper;
import com.eadl.connect_backend.domain.model.user.Client;
import com.eadl.connect_backend.domain.model.user.User;
import com.eadl.connect_backend.domain.port.in.user.ProfileService;
import com.eadl.connect_backend.domain.port.in.user.UserService;
import com.eadl.connect_backend.domain.port.out.security.CurrentUserProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User Profile", description = "Endpoints for managing user profile")
public class UserController {

    private final UserService userService;
    private final ProfileService profileService;
    private final CurrentUserProvider currentUserProvider;

    @Operation(summary = "Get current user profile")
    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserDto> getCurrentUser() {
        Long currentUserId = currentUserProvider.getCurrentUserId();
        return userService.getById(currentUserId)
                .map(UserMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get user by ID")
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserDto> getById(@PathVariable Long id) {
        return userService.getById(id)
                .map(UserMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Update current user profile")
    @PutMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserDto> updateProfile(@RequestBody UserDto userDto) {
        // Create a temporary Client object to pass to the service
        // UserServiceImpl only uses firstName, lastName, phone, city, neighborhood
        Client userUpdate = new Client();
        userUpdate.setFirstName(userDto.getFirstName());
        userUpdate.setLastName(userDto.getLastName());
        userUpdate.setPhone(userDto.getPhone());
        userUpdate.setCity(userDto.getCity());
        userUpdate.setNeighborhood(userDto.getNeighborhood());

        User updatedUser = userService.updateProfile(userUpdate);
        return ResponseEntity.ok(UserMapper.toDto(updatedUser));
    }

    @Operation(summary = "Update current user profile photo")
    @PostMapping("/me/photo")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserDto> updatePhoto(@RequestParam("file") MultipartFile file) throws IOException {
        Long currentUserId = currentUserProvider.getCurrentUserId();
        User updatedUser = profileService.updateProfilePhoto(
                currentUserId,
                file.getBytes(),
                file.getOriginalFilename());
        return ResponseEntity.ok(UserMapper.toDto(updatedUser));
    }
}
