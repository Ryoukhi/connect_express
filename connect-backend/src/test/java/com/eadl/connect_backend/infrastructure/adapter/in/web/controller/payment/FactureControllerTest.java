package com.eadl.connect_backend.infrastructure.adapter.in.web.controller.payment;

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

import com.eadl.connect_backend.application.dto.FactureDto;
import com.eadl.connect_backend.domain.model.payment.Facture;
import com.eadl.connect_backend.domain.port.in.payment.FactureService;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class FactureControllerTest {

    private MockMvc mockMvc;

    @Mock
    private FactureService factureService;

    @InjectMocks
    private FactureController factureController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(factureController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void createFacture_ShouldReturnCreatedFacture() throws Exception {
        // Arrange
        FactureDto dto = new FactureDto();
        dto.setIdReservation(1L);
        dto.setAmount(new BigDecimal("100.00"));
        dto.setInvoiceNumber("INV-001");

        FactureDto createdDto = new FactureDto();
        createdDto.setIdFacture(1L);
        createdDto.setInvoiceNumber("INV-001");
        createdDto.setPdfUrl("http://pdf.com");

        when(factureService.createFacture(any(Facture.class))).thenReturn(createdDto);
        when(factureService.generatePdf(any(Facture.class))).thenReturn("http://pdf.com");

        // Act & Assert
        mockMvc.perform(post("/api/factures")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.invoiceNumber").value("INV-001"))
                .andExpect(jsonPath("$.pdfUrl").value("http://pdf.com"));
    }

    @Test
    void updateFacture_ShouldReturnUpdatedFacture() throws Exception {
        // Arrange
        Long id = 1L;
        FactureDto dto = new FactureDto();
        dto.setAmount(new BigDecimal("150.00"));

        FactureDto updatedDto = new FactureDto();
        updatedDto.setIdFacture(id);
        updatedDto.setAmount(new BigDecimal("150.00"));

        when(factureService.updateFacture(eq(id), any(Facture.class))).thenReturn(updatedDto);

        // Act & Assert
        mockMvc.perform(put("/api/factures/{idFacture}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(150.00));
    }

    @Test
    void getFactureById_ShouldReturnFacture_WhenFound() throws Exception {
        // Arrange
        Long id = 1L;
        FactureDto dto = new FactureDto();
        dto.setIdFacture(id);

        when(factureService.getFactureById(id)).thenReturn(Optional.of(dto));

        // Act & Assert
        mockMvc.perform(get("/api/factures/{idFacture}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idFacture").value(id));
    }

    @Test
    void getFactureById_ShouldReturnNotFound_WhenNotFound() throws Exception {
        // Arrange
        Long id = 1L;
        when(factureService.getFactureById(id)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/factures/{idFacture}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllFactures_ShouldReturnListOfFactures() throws Exception {
        // Arrange
        FactureDto dto = new FactureDto();
        dto.setIdFacture(1L);

        when(factureService.getAllFactures()).thenReturn(List.of(dto));

        // Act & Assert
        mockMvc.perform(get("/api/factures"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idFacture").value(1));
    }

    @Test
    void deleteFacture_ShouldReturnNoContent() throws Exception {
        // Arrange
        Long id = 1L;
        doNothing().when(factureService).deleteFacture(id);

        // Act & Assert
        mockMvc.perform(delete("/api/factures/{idFacture}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    void getFacturesByReservation_ShouldReturnListOfFactures() throws Exception {
        // Arrange
        Long reservationId = 1L;
        FactureDto dto = new FactureDto();
        dto.setIdReservation(reservationId);

        when(factureService.getFacturesByReservationId(reservationId)).thenReturn(List.of(dto));

        // Act & Assert
        mockMvc.perform(get("/api/factures/reservation/{idReservation}", reservationId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idReservation").value(reservationId));
    }
}
