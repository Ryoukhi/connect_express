package com.eadl.connect_backend.infrastructure.adapter.in.web.controller.reservation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.eadl.connect_backend.application.dto.ReservationDto;
import com.eadl.connect_backend.application.mapper.ReservationMapper;
import com.eadl.connect_backend.domain.model.reservation.Reservation;
import com.eadl.connect_backend.domain.model.reservation.ReservationStatus;
import com.eadl.connect_backend.domain.port.in.reservation.ReservationService;
import com.eadl.connect_backend.domain.port.out.security.CurrentUserProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@ExtendWith(MockitoExtension.class)
class ReservationControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ReservationService reservationService;

    @Mock
    private ReservationMapper reservationMapper;

    @Mock
    private CurrentUserProvider currentUserProvider;

    @InjectMocks
    private ReservationController reservationController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(reservationController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void createReservation_ShouldReturnCreatedReservation() throws Exception {
        // Arrange
        ReservationDto inputDto = new ReservationDto();
        Reservation reservation = new Reservation();
        Reservation createdReservation = new Reservation();
        ReservationDto outputDto = new ReservationDto();
        outputDto.setIdReservation(1L);

        when(reservationMapper.toModel(any(ReservationDto.class))).thenReturn(reservation);
        when(reservationService.createReservation(any(Reservation.class))).thenReturn(createdReservation);
        when(reservationMapper.toDto(createdReservation)).thenReturn(outputDto);

        // Act & Assert
        mockMvc.perform(post("/api/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idReservation").value(1));
    }

    @Test
    void getById_ShouldReturnReservation_WhenFound() throws Exception {
        // Arrange
        Long id = 1L;
        Reservation reservation = new Reservation();
        ReservationDto dto = new ReservationDto();
        dto.setIdReservation(id);

        when(reservationService.getReservationById(id)).thenReturn(Optional.of(reservation));
        when(reservationMapper.toDto(reservation)).thenReturn(dto);

        // Act & Assert
        mockMvc.perform(get("/api/reservations/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idReservation").value(id));
    }

    @Test
    void getMyClientReservations_ShouldReturnListOfReservations() throws Exception {
        // Arrange
        Long clientId = 1L;
        Reservation reservation = new Reservation();
        ReservationDto dto = new ReservationDto();
        dto.setIdReservation(1L);

        when(currentUserProvider.getCurrentUserId()).thenReturn(clientId);
        when(reservationService.getClientReservations(clientId)).thenReturn(List.of(reservation));
        when(reservationMapper.toDto(reservation)).thenReturn(dto);

        // Act & Assert
        mockMvc.perform(get("/api/reservations/me/client"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idReservation").value(1));
    }

    @Test
    void getMyTechnicianReservations_ShouldReturnListOfReservations() throws Exception {
        // Arrange
        Long technicianId = 1L;
        Reservation reservation = new Reservation();
        ReservationDto dto = new ReservationDto();
        dto.setIdReservation(1L);

        when(currentUserProvider.getCurrentUserId()).thenReturn(technicianId);
        when(reservationService.getTechnicianReservations(technicianId)).thenReturn(List.of(reservation));
        when(reservationMapper.toDto(reservation)).thenReturn(dto);

        // Act & Assert
        mockMvc.perform(get("/api/reservations/me/technician"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idReservation").value(1));
    }

    @Test
    void updateReservation_ShouldReturnUpdatedReservation() throws Exception {
        // Arrange
        Long id = 1L;
        ReservationDto dto = new ReservationDto();
        Reservation reservation = new Reservation();
        Reservation updatedReservation = new Reservation();
        ReservationDto outputDto = new ReservationDto();
        outputDto.setIdReservation(id);

        when(reservationMapper.toModel(any(ReservationDto.class))).thenReturn(reservation);
        when(reservationService.updateReservation(eq(id), any(Reservation.class))).thenReturn(updatedReservation);
        when(reservationMapper.toDto(updatedReservation)).thenReturn(outputDto);

        // Act & Assert
        mockMvc.perform(put("/api/reservations/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idReservation").value(id));
    }

    @Test
    void cancelReservation_ShouldReturnCancelledReservation() throws Exception {
        // Arrange
        Long id = 1L;
        Long userId = 1L;
        String reason = "Cancelled";
        Reservation cancelledReservation = new Reservation();
        ReservationDto outputDto = new ReservationDto();
        outputDto.setIdReservation(id);

        when(currentUserProvider.getCurrentUserId()).thenReturn(userId);
        when(reservationService.cancelReservation(id, userId, reason)).thenReturn(cancelledReservation);
        when(reservationMapper.toDto(cancelledReservation)).thenReturn(outputDto);

        // Act & Assert
        mockMvc.perform(post("/api/reservations/{id}/cancel", id)
                .param("reason", reason))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idReservation").value(id));
    }

    @Test
    void changeStatus_ShouldReturnUpdatedReservation() throws Exception {
        // Arrange
        Long id = 1L;
        ReservationStatus status = ReservationStatus.ACCEPTED;
        Reservation updatedReservation = new Reservation();
        ReservationDto outputDto = new ReservationDto();
        outputDto.setIdReservation(id);
        outputDto.setStatus(status);

        when(reservationService.changeStatus(id, status)).thenReturn(updatedReservation);
        when(reservationMapper.toDto(updatedReservation)).thenReturn(outputDto);

        // Act & Assert
        mockMvc.perform(post("/api/reservations/{id}/status", id)
                .param("status", status.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(status.toString()));
    }

    @Test
    void completeReservation_ShouldReturnCompletedReservation() throws Exception {
        // Arrange
        Long id = 1L;
        Reservation completedReservation = new Reservation();
        ReservationDto outputDto = new ReservationDto();
        outputDto.setIdReservation(id);
        outputDto.setStatus(ReservationStatus.COMPLETED);

        when(reservationService.completeReservation(id)).thenReturn(completedReservation);
        when(reservationMapper.toDto(completedReservation)).thenReturn(outputDto);

        // Act & Assert
        mockMvc.perform(post("/api/reservations/{id}/complete", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("COMPLETED"));
    }

    @Test
    void checkAvailability_ShouldReturnBoolean() throws Exception {
        // Arrange
        Long technicianId = 1L;
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusHours(1);

        when(reservationService.isTechnicianAvailable(eq(technicianId), any(LocalDateTime.class),
                any(LocalDateTime.class)))
                .thenReturn(true);

        // Act & Assert
        mockMvc.perform(get("/api/reservations/availability")
                .param("technicianId", technicianId.toString())
                .param("start", start.toString())
                .param("end", end.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(true));
    }

    @Test
    void deleteReservation_ShouldReturnNoContent() throws Exception {
        // Arrange
        Long id = 1L;
        Long adminId = 1L;

        when(currentUserProvider.getCurrentUserId()).thenReturn(adminId);
        doNothing().when(reservationService).deleteReservation(id, adminId);

        // Act & Assert
        mockMvc.perform(delete("/api/reservations/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    void countReservations_ShouldReturnCount() throws Exception {
        // Arrange
        Long count = 10L;
        when(reservationService.countReservations()).thenReturn(count);

        // Act & Assert
        mockMvc.perform(get("/api/reservations/stats/count"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(count));
    }

    @Test
    void getTechnicianRevenue_ShouldReturnRevenue() throws Exception {
        // Arrange
        Long technicianId = 1L;
        BigDecimal revenue = new BigDecimal("1000.00");
        when(reservationService.getTechnicianRevenue(technicianId)).thenReturn(revenue);

        // Act & Assert
        mockMvc.perform(get("/api/reservations/stats/revenue/{technicianId}", technicianId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(1000.00));
    }

    @Test
    void getTechnicianAverageRating_ShouldReturnRating() throws Exception {
        // Arrange
        Long technicianId = 1L;
        Double rating = 4.5;
        when(reservationService.getTechnicianAverageRating(technicianId)).thenReturn(rating);

        // Act & Assert
        mockMvc.perform(get("/api/reservations/stats/rating/{technicianId}", technicianId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(4.5));
    }
}
