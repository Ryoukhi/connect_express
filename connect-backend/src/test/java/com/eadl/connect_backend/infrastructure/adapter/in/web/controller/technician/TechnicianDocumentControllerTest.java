package com.eadl.connect_backend.infrastructure.adapter.in.web.controller.technician;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

import com.eadl.connect_backend.application.dto.TechnicianDocumentDto;
import com.eadl.connect_backend.domain.model.technician.TechnicianDocument;
import com.eadl.connect_backend.domain.port.in.technician.TechnicianDocumentService;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class TechnicianDocumentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TechnicianDocumentService documentService;

    @InjectMocks
    private TechnicianDocumentController documentController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(documentController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void addDocument_ShouldReturnCreatedDocument() throws Exception {
        // Arrange
        TechnicianDocumentDto dto = new TechnicianDocumentDto();
        dto.setVerificationNote("Note");
        dto.setUrl("http://url.com");

        TechnicianDocument document = new TechnicianDocument();
        document.setIdDocument(1L);
        document.setUrl("http://url.com");

        TechnicianDocumentDto outputDto = new TechnicianDocumentDto();
        outputDto.setIdDocument(1L);
        outputDto.setUrl("http://url.com");

        when(documentService.addDocument(any(TechnicianDocument.class))).thenReturn(document);

        // Act & Assert
        mockMvc.perform(post("/api/technician-documents")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.url").value("http://url.com"));
    }

    @Test
    void getBySkill_ShouldReturnListOfDocuments() throws Exception {
        // Arrange
        Long skillId = 1L;
        TechnicianDocument document = new TechnicianDocument();
        document.setIdDocument(1L);

        when(documentService.getDocumentsByTechnicianSkillId(skillId)).thenReturn(List.of(document));

        // Act & Assert
        mockMvc.perform(get("/api/technician-documents/skill/{skillId}", skillId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idDocument").value(1));
    }

    @Test
    void getById_ShouldReturnDocument_WhenFound() throws Exception {
        // Arrange
        Long id = 1L;
        TechnicianDocument document = new TechnicianDocument();
        document.setIdDocument(id);

        when(documentService.getDocumentById(id)).thenReturn(Optional.of(document));

        // Act & Assert
        mockMvc.perform(get("/api/technician-documents/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idDocument").value(id));
    }

    @Test
    void getById_ShouldReturnNotFound_WhenNotFound() throws Exception {
        // Arrange
        Long id = 1L;
        when(documentService.getDocumentById(id)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/technician-documents/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void verifyDocument_ShouldReturnOk() throws Exception {
        // Arrange
        Long id = 1L;
        String note = "Verified";
        doNothing().when(documentService).verifyDocument(id, note);

        // Act & Assert
        mockMvc.perform(post("/api/technician-documents/{id}/verify", id)
                .param("note", note))
                .andExpect(status().isOk());
    }

    @Test
    void rejectDocument_ShouldReturnOk() throws Exception {
        // Arrange
        Long id = 1L;
        String note = "Rejected";
        doNothing().when(documentService).rejectDocument(id, note);

        // Act & Assert
        mockMvc.perform(post("/api/technician-documents/{id}/reject", id)
                .param("note", note))
                .andExpect(status().isOk());
    }

    @Test
    void deleteDocument_ShouldReturnNoContent() throws Exception {
        // Arrange
        Long id = 1L;
        doNothing().when(documentService).deleteDocument(id);

        // Act & Assert
        mockMvc.perform(delete("/api/technician-documents/{id}", id))
                .andExpect(status().isNoContent());
    }
}
