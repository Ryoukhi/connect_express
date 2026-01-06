package com.eadl.connect_backend.infrastructure.adapter.in.web.controller.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.eadl.connect_backend.application.dto.AuthRequest;
import com.eadl.connect_backend.application.dto.AuthResponse;
import com.eadl.connect_backend.application.dto.RegisterDto;
import com.eadl.connect_backend.application.dto.RegisterResponseDto;
import com.eadl.connect_backend.application.mapper.RegisterMapper;
import com.eadl.connect_backend.domain.model.user.Client;
import com.eadl.connect_backend.domain.port.in.user.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final RegisterMapper registerMapper;

    /**
     * Login utilisateur
     */
    @Operation(summary = "Connecte un utilisateur et retourne un token JWT")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Connexion réussie",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class))),
        @ApiResponse(responseCode = "401", description = "Identifiants invalides")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {
        AuthResponse response = authService.login(authRequest);
        return ResponseEntity.ok(response);
    }

    /**
     * Logout utilisateur
     */
    @Operation(summary = "Déconnecte un utilisateur")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Utilisateur déconnecté")
    })
    @PostMapping("/logout/{idUser}")
    public ResponseEntity<Void> logout(@PathVariable Long idUser) {
        authService.logout(idUser);
        return ResponseEntity.noContent().build();
    }

    /**
     * Inscription utilisateur
     */
    @Operation(summary = "Inscrit un nouvel utilisateur")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Utilisateur créé avec succès",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = RegisterResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDto> register(@Valid @RequestBody RegisterDto registerDto) {
        Client client = registerMapper.toModel(registerDto);
        RegisterResponseDto createdUser = authService.register(client);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }
}