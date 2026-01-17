package com.eadl.connect_backend.infrastructure.adapter.in.web.controller.client;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.eadl.connect_backend.application.dto.ClientDto;
import com.eadl.connect_backend.application.dto.ReservationDto;
import com.eadl.connect_backend.application.mapper.ClientMapper;
import com.eadl.connect_backend.application.mapper.ReservationMapper;
import com.eadl.connect_backend.domain.model.reservation.Reservation;
import com.eadl.connect_backend.domain.model.user.Client;
import com.eadl.connect_backend.domain.port.in.client.ClientService;

@ExtendWith(MockitoExtension.class)
class ClientControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ClientService clientService;

    @Mock
    private ClientMapper clientMapper;

    @Mock
    private ReservationMapper reservationMapper;

    @InjectMocks
    private ClientController clientController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(clientController).build();
    }

    @Test
    void getMyReservations_ShouldReturnListOfReservations() throws Exception {
        // Arrange
        Reservation reservation = new Reservation();
        ReservationDto dto = new ReservationDto();
        dto.setIdReservation(1L);

        when(clientService.getClientReservations()).thenReturn(List.of(reservation));
        when(reservationMapper.toDto(reservation)).thenReturn(dto);

        // Act & Assert
        mockMvc.perform(get("/api/clients/me/reservations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idReservation").value(1));
    }

    @Test
    void getActiveClients_ShouldReturnListOfClients() throws Exception {
        // Arrange
        Client client = new Client();
        ClientDto dto = new ClientDto();
        dto.setFirstName("John");

        when(clientService.getActiveClients()).thenReturn(List.of(client));
        when(clientMapper.toDto(client)).thenReturn(dto);

        // Act & Assert
        mockMvc.perform(get("/api/clients/active"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("John"));
    }

    @Test
    void getClientsByCity_ShouldReturnListOfClients() throws Exception {
        // Arrange
        String city = "Paris";
        Client client = new Client();
        ClientDto dto = new ClientDto();
        dto.setCity(city);

        when(clientService.getClientsByCity(city)).thenReturn(List.of(client));
        when(clientMapper.toDto(client)).thenReturn(dto);

        // Act & Assert
        mockMvc.perform(get("/api/clients/by-city").param("city", city))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].city").value(city));
    }

    @Test
    void countActiveClients_ShouldReturnCount() throws Exception {
        // Arrange
        Long count = 10L;
        when(clientService.countActiveClients()).thenReturn(count);

        // Act & Assert
        mockMvc.perform(get("/api/clients/stats/count-active"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(count));
    }
}
