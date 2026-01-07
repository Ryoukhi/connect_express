package com.eadl.connect_backend.infrastructure.adapter.in.web.controller.admin;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eadl.connect_backend.application.dto.RegisterAdminDto;
import com.eadl.connect_backend.application.mapper.RegisterAdminMapper;
import com.eadl.connect_backend.domain.model.user.Admin;
import com.eadl.connect_backend.domain.port.in.admin.AdminService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
@Tag(name = "Admin Users", description = "Endpoints for managing admin users")
public class AdminUserController {

    private final AdminService adminService;
    private final RegisterAdminMapper adminMapper;

    // ================== CREATE ADMIN ==================

    @Operation(summary = "Create a new admin user", description = "Creates a new admin and returns the created admin details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Admin created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping("/admins")
    public ResponseEntity<RegisterAdminDto> createAdmin(
            @RequestBody RegisterAdminDto dto
    ) {
        Admin admin = adminMapper.toModel(dto);
        Admin createdAdmin = adminService.createAdmin(admin);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(adminMapper.toDto(createdAdmin));
    }

    // ================== SUSPEND USER ==================

    @Operation(summary = "Suspend a user", description = "Suspends a user account by their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User suspended successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PatchMapping("/{userId}/suspend")
    public ResponseEntity<Void> suspendUser(
            @Parameter(description = "ID of the user to suspend") @PathVariable Long userId
    ) {
        adminService.suspendUser(userId);
        return ResponseEntity.noContent().build();
    }

    // ================== ACTIVATE USER ==================

    @Operation(summary = "Activate a user", description = "Activates a suspended user account by their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User activated successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PatchMapping("/{userId}/activate")
    public ResponseEntity<Void> activateUser(
            @Parameter(description = "ID of the user to activate") @PathVariable Long userId
    ) {
        adminService.activateUser(userId);
        return ResponseEntity.noContent().build();
    }
}
