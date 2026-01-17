package com.eadl.connect_backend.infrastructure.adapter.in.web.controller.technician;

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

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.eadl.connect_backend.application.dto.TechnicianSkillDto;
import com.eadl.connect_backend.application.mapper.TechnicianSkillMapper;
import com.eadl.connect_backend.domain.model.technician.TechnicianSkill;
import com.eadl.connect_backend.domain.port.in.technician.TechnicianSkillService;
import com.eadl.connect_backend.domain.port.out.security.CurrentUserProvider;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class TechnicianSkillControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TechnicianSkillService technicianSkillService;

    @Mock
    private CurrentUserProvider currentUserProvider;

    @Mock
    private TechnicianSkillMapper technicianSkillMapper;

    @InjectMocks
    private TechnicianSkillController technicianSkillController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(technicianSkillController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void addSkill_ShouldReturnCreatedSkill() throws Exception {
        // Arrange
        Long technicianId = 1L;
        TechnicianSkillDto inputDto = new TechnicianSkillDto();
        inputDto.setName("Java");
        inputDto.setDescription("Expert level");

        TechnicianSkill skill = new TechnicianSkill();
        skill.setIdSkill(1L);
        skill.setName("Java");

        TechnicianSkill createdSkill = new TechnicianSkill();
        createdSkill.setIdSkill(1L);
        createdSkill.setName("Java");

        TechnicianSkillDto outputDto = new TechnicianSkillDto();
        outputDto.setIdSkill(1L);
        outputDto.setName("Java");

        when(currentUserProvider.getCurrentUserId()).thenReturn(technicianId);
        when(technicianSkillMapper.toModel(any(TechnicianSkillDto.class))).thenReturn(skill);
        when(technicianSkillService.addSkill(any(TechnicianSkill.class))).thenReturn(createdSkill);
        when(technicianSkillMapper.toDto(createdSkill)).thenReturn(outputDto);

        // Act & Assert
        mockMvc.perform(post("/api/technician-skills")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Java"));
    }

    @Test
    void getMySkills_ShouldReturnListOfSkills() throws Exception {
        // Arrange
        Long technicianId = 1L;
        TechnicianSkill skill = new TechnicianSkill();
        TechnicianSkillDto dto = new TechnicianSkillDto();
        dto.setIdSkill(1L);

        when(currentUserProvider.getCurrentUserId()).thenReturn(technicianId);
        when(technicianSkillService.getSkillsByTechnicianId(technicianId)).thenReturn(List.of(skill));
        when(technicianSkillMapper.toDtoList(any())).thenReturn(List.of(dto));

        // Act & Assert
        mockMvc.perform(get("/api/technician-skills/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idSkill").value(1));
    }

    @Test
    void getSkillsByCategory_ShouldReturnListOfSkills() throws Exception {
        // Arrange
        Long technicianId = 1L;
        Long categoryId = 2L;
        TechnicianSkill skill = new TechnicianSkill();
        TechnicianSkillDto dto = new TechnicianSkillDto();
        dto.setIdSkill(1L);

        when(currentUserProvider.getCurrentUserId()).thenReturn(technicianId);
        when(technicianSkillService.getSkillsByCategory(technicianId, categoryId)).thenReturn(List.of(skill));
        when(technicianSkillMapper.toDtoList(any())).thenReturn(List.of(dto));

        // Act & Assert
        mockMvc.perform(get("/api/technician-skills/category/{categoryId}", categoryId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idSkill").value(1));
    }

    @Test
    void updateSkill_ShouldReturnUpdatedSkill() throws Exception {
        // Arrange
        Long technicianId = 1L;
        Long skillId = 10L;
        TechnicianSkillDto inputDto = new TechnicianSkillDto();
        inputDto.setName("Updated Repair");
        TechnicianSkill skill = new TechnicianSkill();
        TechnicianSkill updatedSkill = new TechnicianSkill();
        updatedSkill.setIdSkill(skillId);
        TechnicianSkillDto outputDto = new TechnicianSkillDto();
        outputDto.setIdSkill(skillId);
        outputDto.setName("Updated Repair");

        when(currentUserProvider.getCurrentUserId()).thenReturn(technicianId);
        when(technicianSkillMapper.toModel(any(TechnicianSkillDto.class))).thenReturn(skill);
        when(technicianSkillService.updateSkill(eq(skillId), any(TechnicianSkill.class))).thenReturn(updatedSkill);
        when(technicianSkillMapper.toDto(updatedSkill)).thenReturn(outputDto);

        // Act & Assert
        mockMvc.perform(put("/api/technician-skills/{idSkill}", skillId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Repair"));
    }

    @Test
    void verifySkill_ShouldReturnUpdatedSkill() throws Exception {
        // Arrange
        Long skillId = 1L;
        boolean verified = true;
        TechnicianSkill skill = new TechnicianSkill();
        TechnicianSkillDto dto = new TechnicianSkillDto();
        dto.setIdSkill(skillId);
        dto.setVerified(verified);

        when(technicianSkillService.verifySkill(skillId, verified)).thenReturn(skill);
        when(technicianSkillMapper.toDto(skill)).thenReturn(dto);

        // Act & Assert
        mockMvc.perform(post("/api/technician-skills/{idSkill}/verify", skillId)
                .param("verified", String.valueOf(verified)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.verified").value(true));
    }

    @Test
    void deleteSkill_ShouldReturnNoContent() throws Exception {
        // Arrange
        Long technicianId = 1L;
        Long skillId = 10L;

        when(currentUserProvider.getCurrentUserId()).thenReturn(technicianId);
        doNothing().when(technicianSkillService).deleteSkill(skillId, technicianId);

        // Act & Assert
        mockMvc.perform(delete("/api/technician-skills/{idSkill}", skillId))
                .andExpect(status().isNoContent());
    }
}
