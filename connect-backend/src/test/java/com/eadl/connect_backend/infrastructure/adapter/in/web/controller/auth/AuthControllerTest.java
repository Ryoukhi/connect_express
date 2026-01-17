package com.eadl.connect_backend.infrastructure.adapter.in.web.controller.auth;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.eadl.connect_backend.application.dto.AuthRequest;
import com.eadl.connect_backend.application.dto.AuthResponse;
import com.eadl.connect_backend.application.dto.RegisterDto;
import com.eadl.connect_backend.application.dto.RegisterResponseDto;
import com.eadl.connect_backend.application.mapper.RegisterMapper;
import com.eadl.connect_backend.domain.model.user.Client;
import com.eadl.connect_backend.domain.model.user.Role;
import com.eadl.connect_backend.domain.model.user.Technician;
import com.eadl.connect_backend.domain.port.in.user.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

        private MockMvc mockMvc;

        @Mock
        private AuthService authService;

        @Mock
        private RegisterMapper registerMapper;

        @InjectMocks
        private AuthController authController;

        private ObjectMapper objectMapper;

        @BeforeEach
        void setUp() {
                mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
                objectMapper = new ObjectMapper();
        }

        @Test
        void login_ShouldReturnAuthResponse() throws Exception {
                // Arrange
                AuthRequest request = new AuthRequest();
                request.setEmail("test@example.com");
                request.setPassword("password");

                AuthResponse response = new AuthResponse("jwt-token", 1L, "Test", "User",
                        Role.CLIENT, true, "http://image.jpg");

                when(authService.login(any(AuthRequest.class))).thenReturn(response);

                // Act & Assert
                mockMvc.perform(post("/api/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.token").value("jwt-token"));
        }

        @Test
        void logout_ShouldReturnNoContent() throws Exception {
                // Arrange
                Long userId = 1L;
                doNothing().when(authService).logout(userId);

                // Act & Assert
                mockMvc.perform(post("/api/auth/logout/{idUser}", userId))
                                .andExpect(status().isNoContent());
        }

        @Test
        void register_ShouldReturnRegisterResponse() throws Exception {
                // Arrange
                RegisterDto registerDto = new RegisterDto();
                registerDto.setFirstName("Client");
                registerDto.setLastName("User");
                registerDto.setEmail("client@example.com");
                registerDto.setPassword("Password123!");
                registerDto.setPhone("1234567890");
                registerDto.setCity("Address");
                registerDto.setNeighborhood("Neighborhood");

                Client client = new Client();
                RegisterResponseDto responseDto = new RegisterResponseDto();
                responseDto.setEmail("client@example.com");
                responseDto.setFirstName("Client");
                responseDto.setLastName("User");

                when(registerMapper.toModel(any(RegisterDto.class))).thenReturn(client);
                when(authService.register(any(Client.class))).thenReturn(responseDto);

                // Act & Assert
                mockMvc.perform(post("/api/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(registerDto)))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.email").value("client@example.com"));
        }

        @Test
        void registerTechnician_ShouldReturnRegisterResponse() throws Exception {
                // Arrange
                RegisterDto registerDto = new RegisterDto();
                registerDto.setFirstName("Tech");
                registerDto.setLastName("User");
                registerDto.setEmail("tech@example.com");
                registerDto.setPassword("Password123!");
                registerDto.setPhone("1234567890");
                registerDto.setCity("Address");
                registerDto.setNeighborhood("Neighborhood");

                Technician technician = new Technician();
                RegisterResponseDto responseDto = new RegisterResponseDto();
                responseDto.setEmail("tech@example.com");
                responseDto.setFirstName("Tech");
                responseDto.setLastName("User");

                when(registerMapper.toModelTechnician(any(RegisterDto.class))).thenReturn(technician);
                when(authService.register(any(Technician.class))).thenReturn(responseDto);

                // Act & Assert
                mockMvc.perform(post("/api/auth/register-technician")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(registerDto)))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.email").value("tech@example.com"));
        }
}
