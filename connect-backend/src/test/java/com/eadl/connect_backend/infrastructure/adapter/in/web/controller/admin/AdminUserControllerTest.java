package com.eadl.connect_backend.infrastructure.adapter.in.web.controller.admin;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
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

import com.eadl.connect_backend.application.dto.RegisterAdminDto;
import com.eadl.connect_backend.application.mapper.RegisterAdminMapper;
import com.eadl.connect_backend.domain.model.user.Admin;
import com.eadl.connect_backend.domain.port.in.admin.AdminService;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class AdminUserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AdminService adminService;

    @Mock
    private RegisterAdminMapper adminMapper;

    @InjectMocks
    private AdminUserController adminUserController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(adminUserController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void createAdmin_ShouldReturnCreatedAdmin() throws Exception {
        // Arrange
        RegisterAdminDto inputDto = new RegisterAdminDto();
        inputDto.setFirstName("Admin");
        inputDto.setLastName("User");
        inputDto.setEmail("admin@example.com");
        inputDto.setPassword("password");
        inputDto.setPhone("1234567890");
        inputDto.setCity("Address");

        Admin adminModel = new Admin();
        adminModel.setIdUser(1L);
        adminModel.setEmail("admin@example.com");

        RegisterAdminDto outputDto = new RegisterAdminDto();
        outputDto.setFirstName("Admin");
        outputDto.setLastName("User");
        outputDto.setEmail("admin@example.com");
        outputDto.setPhone("1234567890");
        outputDto.setCity("Address");

        when(adminMapper.toModel(any(RegisterAdminDto.class))).thenReturn(adminModel);
        when(adminService.createAdmin(any(Admin.class))).thenReturn(adminModel);
        when(adminMapper.toDto(any(Admin.class))).thenReturn(outputDto);

        // Act & Assert
        mockMvc.perform(post("/api/admin/users/admins")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("admin@example.com"));
    }

    @Test
    void suspendUser_ShouldReturnNoContent() throws Exception {
        // Arrange
        Long userId = 1L;
        doNothing().when(adminService).suspendUser(userId);

        // Act & Assert
        mockMvc.perform(patch("/api/admin/users/{userId}/suspend", userId))
                .andExpect(status().isNoContent());
    }

    @Test
    void activateUser_ShouldReturnNoContent() throws Exception {
        // Arrange
        Long userId = 1L;
        doNothing().when(adminService).activateUser(userId);

        // Act & Assert
        mockMvc.perform(patch("/api/admin/users/{userId}/activate", userId))
                .andExpect(status().isNoContent());
    }
}
