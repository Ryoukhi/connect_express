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

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminService adminService;
    private final RegisterAdminMapper adminMapper;

    // ================== CREATE ADMIN ==================

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

    @PatchMapping("/{userId}/suspend")
    public ResponseEntity<Void> suspendUser(@PathVariable Long userId) {
        adminService.suspendUser(userId);
        return ResponseEntity.noContent().build();
    }

    // ================== ACTIVATE USER ==================

    @PatchMapping("/{userId}/activate")
    public ResponseEntity<Void> activateUser(@PathVariable Long userId) {
        adminService.activateUser(userId);
        return ResponseEntity.noContent().build();
    }
}