package com.eadl.connect_backend.infrastructure.adapter.in.web.controller.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.eadl.connect_backend.application.dto.AuthRequest;
import com.eadl.connect_backend.application.dto.AuthResponse;
import com.eadl.connect_backend.application.dto.RegisterDto;
import com.eadl.connect_backend.application.mapper.RegisterMapper;
import com.eadl.connect_backend.domain.model.user.Client;
import com.eadl.connect_backend.domain.model.user.User;
import com.eadl.connect_backend.domain.port.in.user.AuthService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final RegisterMapper registerMapper;

    /**
     * Login utilisateur
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {
        AuthResponse response = authService.login(authRequest);
        return ResponseEntity.ok(response);
    }

    /**
     * Logout utilisateur
     */
    @PostMapping("/logout/{idUser}")
    public ResponseEntity<Void> logout(@PathVariable Long idUser) {
        authService.logout(idUser);
        return ResponseEntity.noContent().build();
    }

    /**
     * Inscription utilisateur
     */
     @PostMapping("/register")
        public ResponseEntity<RegisterDto> register(
                @RequestBody RegisterDto registerDto
        ) {
            Client client = registerMapper.toModel(registerDto);

            User createdUser = authService.register(client);

            RegisterDto response = registerMapper.toDto(createdUser);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }
}