package com.eadl.connect_backend.application.service.client;

import com.eadl.connect_backend.domain.model.reservation.Reservation;
import com.eadl.connect_backend.domain.model.user.Client;
import com.eadl.connect_backend.domain.port.out.persistence.ClientRepository;
import com.eadl.connect_backend.domain.port.out.persistence.ReservationRepository;
import com.eadl.connect_backend.domain.port.out.security.CurrentUserProvider;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceImplTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private CurrentUserProvider currentUserProvider;

    @InjectMocks
    private ClientServiceImpl clientService;

    private Client client;

    @BeforeEach
    void setUp() {
        client = new Client();
        client.setCity("Douala");
    }

    @Test
    void registerClient_shouldActivateAndSaveClient() {
        // Given
        when(clientRepository.save(any(Client.class))).thenReturn(client);

        // When
        Client savedClient = clientService.registerClient(client);

        // Then
        assertThat(savedClient).isNotNull();
        assertThat(savedClient.isActive()).isTrue();
        verify(clientRepository).save(client);
    }

    @Test
    void getActiveClients_shouldReturnOnlyActiveClients() {
        // Given
        List<Client> clients = List.of(new Client(), new Client());
        when(clientRepository.findByActive(true)).thenReturn(clients);

        // When
        List<Client> result = clientService.getActiveClients();

        // Then
        assertThat(result).hasSize(2);
        verify(clientRepository).findByActive(true);
    }

    @Test
    void getClientsByCity_shouldReturnClientsForCity() {
        // Given
        String city = "Douala";
        List<Client> clients = List.of(client);
        when(clientRepository.findByCity(city)).thenReturn(clients);

        // When
        List<Client> result = clientService.getClientsByCity(city);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCity()).isEqualTo(city);
        verify(clientRepository).findByCity(city);
    }

    @Test
    void getClientsByCity_shouldThrowExceptionWhenCityIsBlank() {
        // When / Then
        assertThatThrownBy(() -> clientService.getClientsByCity(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("City must not be empty");

        verify(clientRepository, never()).findByCity(any());
    }

    @Test
    void getClientReservations_shouldReturnReservationsOfCurrentClient() {
        // Given
        Long clientId = 1L;
        List<Reservation> reservations = List.of(new Reservation(), new Reservation());

        when(currentUserProvider.getCurrentUserId()).thenReturn(clientId);
        when(reservationRepository.findByClientId(clientId)).thenReturn(reservations);

        // When
        List<Reservation> result = clientService.getClientReservations();

        // Then
        assertThat(result).hasSize(2);
        verify(currentUserProvider).getCurrentUserId();
        verify(reservationRepository).findByClientId(clientId);
    }

    @Test
    void countActiveClients_shouldReturnCorrectCount() {
        // Given
        when(clientRepository.countByActive(true)).thenReturn(5L);

        // When
        Long count = clientService.countActiveClients();

        // Then
        assertThat(count).isEqualTo(5L);
        verify(clientRepository).countByActive(true);
    }
}
