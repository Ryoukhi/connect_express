package com.eadl.connect_backend.infrastructure.adapter.in.web.controller.technician;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

import com.eadl.connect_backend.application.dto.TechnicianResultSearchDto;
import com.eadl.connect_backend.domain.model.user.Technician;
import com.eadl.connect_backend.domain.port.in.technician.TechnicianService;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class TechnicianControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TechnicianService technicianService;

    @InjectMocks
    private TechnicianController technicianController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(technicianController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
    }

    @Test
    void registerTechnician_ShouldReturnCreatedTechnician() throws Exception {
        // Arrange
        Technician technician = new Technician();
        technician.setEmail("tech@test.com");

        when(technicianService.registerTechnician(any(Technician.class))).thenReturn(technician);

        // Act & Assert
        mockMvc.perform(post("/api/technicians")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(technician)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("tech@test.com"));
    }

    @Test
    void validateKyc_ShouldReturnNoContent() throws Exception {
        // Arrange
        Long technicianId = 1L;
        doNothing().when(technicianService).validateKyc(technicianId);

        // Act & Assert
        mockMvc.perform(put("/api/technicians/{technicianId}/validate-kyc", technicianId))
                .andExpect(status().isNoContent());
    }

    @Test
    void getActiveTechnicians_ShouldReturnListOfTechnicians() throws Exception {
        // Arrange
        Technician technician = new Technician();
        technician.setIdUser(1L);

        when(technicianService.getActiveTechnicians()).thenReturn(List.of(technician));

        // Act & Assert
        mockMvc.perform(get("/api/technicians/active"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idUser").value(1));
    }

    @Test
    void getTechnicianById_ShouldReturnTechnician_WhenFound() throws Exception {
        // Arrange
        Long technicianId = 1L;
        Technician technician = new Technician();
        technician.setIdUser(technicianId);

        when(technicianService.getTechnicianById(technicianId)).thenReturn(Optional.of(technician));

        // Act & Assert
        mockMvc.perform(get("/api/technicians/{technicianId}", technicianId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idUser").value(technicianId));
    }

    @Test
    void getTechnicianById_ShouldReturnNotFound_WhenNotFound() throws Exception {
        // Arrange
        Long technicianId = 1L;
        when(technicianService.getTechnicianById(technicianId)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/technicians/{technicianId}", technicianId))
                .andExpect(status().isNotFound());
    }

    @Test
    void getByCity_ShouldReturnListOfTechnicians() throws Exception {
        // Arrange
        String city = "Paris";
        Technician technician = new Technician();
        technician.setCity(city);

        when(technicianService.getTechniciansByCity(city)).thenReturn(List.of(technician));

        // Act & Assert
        mockMvc.perform(get("/api/technicians/search/city").param("city", city))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].city").value(city));
    }

    @Test
    void getByNeighborhood_ShouldReturnListOfTechnicians() throws Exception {
        // Arrange
        String neighborhood = "Downtown";
        Technician technician = new Technician();
        technician.setNeighborhood(neighborhood);

        when(technicianService.getTechniciansByNeighborhood(neighborhood)).thenReturn(List.of(technician));

        // Act & Assert
        mockMvc.perform(get("/api/technicians/search/neighborhood").param("neighborhood", neighborhood))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].neighborhood").value(neighborhood));
    }

    @Test
    void countActiveTechnicians_ShouldReturnCount() throws Exception {
        // Arrange
        Long count = 5L;
        when(technicianService.countActiveTechnicians()).thenReturn(count);

        // Act & Assert
        mockMvc.perform(get("/api/technicians/stats/active/count"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(count));
    }

    @Test
    void searchTechnicians_ShouldReturnListOfTechnicians() throws Exception {
        // Arrange
        TechnicianResultSearchDto dto = new TechnicianResultSearchDto(
                1L, "Name", true, 4.5, com.eadl.connect_backend.domain.model.technician.AvailabilityStatus.AVAILABLE,
                50.0, "Skill", 5, "City", "Neighborhood", "http://photo.jpg");

        when(technicianService.searchTechnicians(any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(List.of(dto));

        // Act & Assert
        mockMvc.perform(get("/api/technicians/search"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Name"));
    }
}
