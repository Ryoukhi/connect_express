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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
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
        client.setId(1L);
        client.setFirstName("John");
        client.setLastName("Doe");
        client.setCity("Paris");
        client.setActive(true);
    }

    // ================= REGISTER =================

    @Test
    void shouldRegisterClientSuccessfully() {
        when(clientRepository.save(any(Client.class))).thenReturn(client);

        Client result = clientService.registerClient(client);

        assertThat(result).isNotNull();
        assertThat(result.isActive()).isTrue(); // Service sets activate()
        verify(clientRepository).save(client);
    }

    // ================= READ =================

    @Test
    void shouldGetActiveClients() {
        when(clientRepository.findByActive(true)).thenReturn(List.of(client));

        List<Client> result = clientService.getActiveClients();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFirstName()).isEqualTo("John");
    }

    @Test
    void shouldGetClientsByCity() {
        when(clientRepository.findByCity("Paris")).thenReturn(List.of(client));

        List<Client> result = clientService.getClientsByCity("Paris");

        assertThat(result).hasSize(1);
    }

    @Test
    void shouldThrowIfCityIsEmpty() {
        assertThatThrownBy(() -> clientService.getClientsByCity(""))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldCountActiveClients() {
        when(clientRepository.countByActive(true)).thenReturn(10L);

        Long count = clientService.countActiveClients();

        assertThat(count).isEqualTo(10L);
    }

    // ================= RESERVATIONS =================

    @Test
    void shouldGetClientReservations() {
        when(currentUserProvider.getCurrentUserId()).thenReturn(1L);
        when(reservationRepository.findByClientId(1L)).thenReturn(List.of(new Reservation()));

        List<Reservation> result = clientService.getClientReservations();

        assertThat(result).hasSize(1);
    }
}
