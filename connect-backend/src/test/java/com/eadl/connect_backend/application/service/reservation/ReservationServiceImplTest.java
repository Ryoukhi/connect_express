package com.eadl.connect_backend.application.service.reservation;

import com.eadl.connect_backend.domain.model.reservation.Reservation;
import com.eadl.connect_backend.domain.model.reservation.ReservationStatus;
import com.eadl.connect_backend.domain.port.exception.AvailabilityConflictException;
import com.eadl.connect_backend.domain.port.exception.BusinessException;
import com.eadl.connect_backend.domain.port.exception.ReservationNotFoundException;
import com.eadl.connect_backend.domain.port.out.persistence.ReservationRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceImplTest {

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ReservationServiceImpl reservationService;

    private Reservation reservation;

    @BeforeEach
    void setUp() {
        reservation = new Reservation();
        reservation.setIdClient(1L);
        reservation.setIdTechnician(2L);
        reservation.setScheduledTime(LocalDateTime.now().plusDays(1));
        reservation.setStatus(ReservationStatus.PENDING);
    }

    // ================= CREATE =================

    @Test
    void shouldCreateReservationSuccessfully() {
        when(reservationRepository.existsTechnicianReservationBetween(
                anyLong(), any(), any())).thenReturn(false);

        when(reservationRepository.save(any())).thenReturn(reservation);

        Reservation result = reservationService.createReservation(reservation);

        assertThat(result.getStatus()).isEqualTo(ReservationStatus.PENDING);
        assertThat(result.getCreatedAt()).isNotNull();
        verify(reservationRepository).save(reservation);
    }

    @Test
    void shouldThrowIfReservationDataIsInvalid() {
        Reservation invalid = new Reservation();

        assertThatThrownBy(() ->
                reservationService.createReservation(invalid))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Invalid reservation data");
    }

    @Test
    void shouldThrowIfReservationTimeIsInPast() {
        reservation.setScheduledTime(LocalDateTime.now().minusHours(1));

        assertThatThrownBy(() ->
                reservationService.createReservation(reservation))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("must be in the future");
    }

    @Test
    void shouldThrowIfTechnicianNotAvailable() {
        when(reservationRepository.existsTechnicianReservationBetween(
                anyLong(), any(), any())).thenReturn(true);

        assertThatThrownBy(() ->
                reservationService.createReservation(reservation))
                .isInstanceOf(AvailabilityConflictException.class);
    }

    // ================= READ =================

    @Test
    void shouldReturnReservationById() {
        when(reservationRepository.findById(1L))
                .thenReturn(Optional.of(reservation));

        Optional<Reservation> result =
                reservationService.getReservationById(1L);

        assertThat(result).isPresent();
    }

    @Test
    void shouldReturnClientReservations() {
        when(reservationRepository.findByClientId(1L))
                .thenReturn(List.of(reservation));

        List<Reservation> results =
                reservationService.getClientReservations(1L);

        assertThat(results).hasSize(1);
    }

    @Test
    void shouldReturnTechnicianReservations() {
        when(reservationRepository.findByTechnicianId(2L))
                .thenReturn(List.of(reservation));

        List<Reservation> results =
                reservationService.getTechnicianReservations(2L);

        assertThat(results).hasSize(1);
    }

    // ================= UPDATE =================

    @Test
    void shouldUpdateReservationSuccessfully() {
        when(reservationRepository.findById(1L))
                .thenReturn(Optional.of(reservation));

        when(reservationRepository.update(eq(1L), any()))
                .thenReturn(reservation);

        Reservation updated = new Reservation();

        Reservation result =
                reservationService.updateReservation(1L, updated);

        assertThat(result).isNotNull();
        verify(reservationRepository).update(eq(1L), any());
    }

    @Test
    void shouldNotUpdateCancelledReservation() {
        reservation.setStatus(ReservationStatus.CANCELLED);

        when(reservationRepository.findById(1L))
                .thenReturn(Optional.of(reservation));

        assertThatThrownBy(() ->
                reservationService.updateReservation(1L, new Reservation()))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Cannot update");
    }

    // ================= CANCEL =================

    @Test
    void shouldCancelReservationSuccessfully() {
        when(reservationRepository.findById(1L))
                .thenReturn(Optional.of(reservation));

        when(reservationRepository.update(eq(1L), any()))
                .thenReturn(reservation);

        Reservation result =
                reservationService.cancelReservation(1L, 99L, "Client request");

        assertThat(result.getStatus()).isEqualTo(ReservationStatus.CANCELLED);
        assertThat(result.getCancellationReason()).isEqualTo("Client request");
    }

    @Test
    void shouldThrowIfAlreadyCancelled() {
        reservation.setStatus(ReservationStatus.CANCELLED);

        when(reservationRepository.findById(1L))
                .thenReturn(Optional.of(reservation));

        assertThatThrownBy(() ->
                reservationService.cancelReservation(1L, 1L, "reason"))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("already cancelled");
    }

    // ================= STATUS =================

    @Test
    void shouldChangeReservationStatus() {
        when(reservationRepository.findById(1L))
                .thenReturn(Optional.of(reservation));

        when(reservationRepository.update(eq(1L), any()))
                .thenReturn(reservation);

        Reservation result =
                reservationService.changeStatus(1L, ReservationStatus.ACCEPTED);

        assertThat(result.getStatus()).isEqualTo(ReservationStatus.ACCEPTED);
    }

    @Test
    void shouldCompleteReservationIfInProgress() {
        reservation.setStatus(ReservationStatus.IN_PROGRESS);

        when(reservationRepository.findById(1L))
                .thenReturn(Optional.of(reservation));

        when(reservationRepository.update(eq(1L), any()))
                .thenReturn(reservation);

        Reservation result =
                reservationService.completeReservation(1L);

        assertThat(result.getStatus()).isEqualTo(ReservationStatus.COMPLETED);
        assertThat(result.getCompletedAt()).isNotNull();
    }

    @Test
    void shouldThrowIfCompletingNonAcceptedReservation() {
        reservation.setStatus(ReservationStatus.PENDING);

        when(reservationRepository.findById(1L))
                .thenReturn(Optional.of(reservation));

        assertThatThrownBy(() ->
                reservationService.completeReservation(1L))
                .isInstanceOf(BusinessException.class);
    }

    // ================= AVAILABILITY =================

    @Test
    void shouldReturnTrueIfTechnicianIsAvailable() {
        when(reservationRepository.existsTechnicianReservationBetween(
                anyLong(), any(), any())).thenReturn(false);

        boolean available = reservationService.isTechnicianAvailable(
                1L,
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1));

        assertThat(available).isTrue();
    }

    // ================= DELETE =================

    @Test
    void shouldDeleteReservation() {
        when(reservationRepository.findById(1L))
                .thenReturn(Optional.of(reservation));

        reservationService.deleteReservation(1L, 1L);

        verify(reservationRepository).delete(reservation);
    }

    // ================= COUNT =================

    @Test
    void shouldCountReservations() {
        when(reservationRepository.count()).thenReturn(5L);

        Long count = reservationService.countReservations();

        assertThat(count).isEqualTo(5L);
    }

    // ================= NOT FOUND =================

    @Test
    void shouldThrowIfReservationNotFound() {
        when(reservationRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                reservationService.cancelReservation(1L, 1L, "reason"))
                .isInstanceOf(ReservationNotFoundException.class);
    }
}
