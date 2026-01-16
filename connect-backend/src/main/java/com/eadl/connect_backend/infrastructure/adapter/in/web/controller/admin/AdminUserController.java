package com.eadl.connect_backend.infrastructure.adapter.in.web.controller.admin;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eadl.connect_backend.application.dto.RegisterAdminDto;
import com.eadl.connect_backend.application.dto.UserDto;
import com.eadl.connect_backend.application.mapper.RegisterAdminMapper;
import com.eadl.connect_backend.application.mapper.UserMapper;
import com.eadl.connect_backend.domain.model.user.Admin;
import com.eadl.connect_backend.domain.model.user.User;
import com.eadl.connect_backend.domain.port.in.admin.AdminService;
import com.eadl.connect_backend.domain.port.in.admin.UserManagementService;
import com.eadl.connect_backend.domain.port.out.security.CurrentUserProvider;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
@Tag(name = "Admin Users", description = "Endpoints for managing users (Admins, Technicians, Clients)")
public class AdminUserController {

        private final AdminService adminService;
        private final UserManagementService userManagementService;
        private final RegisterAdminMapper adminMapper;
        private final CurrentUserProvider currentUserProvider;

        // ================== CREATE ADMIN ==================

        @Operation(summary = "Create a new admin user", description = "Creates a new admin and returns the created admin details")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Admin created successfully"),
                        @ApiResponse(responseCode = "400", description = "Invalid input data")
        })
        @PostMapping("/admins")
        public ResponseEntity<RegisterAdminDto> createAdmin(
                        @RequestBody RegisterAdminDto dto) {
                Admin admin = adminMapper.toModel(dto);
                Admin createdAdmin = adminService.createAdmin(admin);

                return ResponseEntity
                                .status(HttpStatus.CREATED)
                                .body(adminMapper.toDto(createdAdmin));
        }

        // ================== GET USERS ==================

        @Operation(summary = "Get all users with pagination", description = "Retrieve a paginated list of all users")
        @GetMapping
        public ResponseEntity<List<UserDto>> getAllUsers(
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size) {
                List<User> users = userManagementService.getAllUsers(page, size);
                return ResponseEntity.ok(UserMapper.toDtos(users));
        }

        @Operation(summary = "Search users", description = "Search users by name/email/role")
        @GetMapping("/search")
        public ResponseEntity<List<UserDto>> searchUsers(
                        @RequestParam(required = false) String query,
                        @RequestParam(required = false) String role) {
                List<User> users = userManagementService.searchUsers(query, role);
                return ResponseEntity.ok(UserMapper.toDtos(users));
        }

        // ================== SUSPEND / ACTIVATE ==================

        @Operation(summary = "Suspend a user", description = "Suspends a user account by their ID")
        @PatchMapping("/{userId}/suspend")
        public ResponseEntity<Void> suspendUser(
                        @PathVariable Long userId,
                        @RequestParam(required = false, defaultValue = "Admin Action") String reason) {
                Long adminId = currentUserProvider.getCurrentUserId();
                userManagementService.suspendUser(adminId, userId, reason);
                return ResponseEntity.noContent().build();
        }

        @Operation(summary = "Activate a user", description = "Activates a suspended user account by their ID")
        @PatchMapping("/{userId}/activate")
        public ResponseEntity<Void> activateUser(
                        @PathVariable Long userId,
                        @RequestParam(required = false, defaultValue = "Admin Action") String reason) {
                Long adminId = currentUserProvider.getCurrentUserId();
                userManagementService.reactivateUser(adminId, userId, reason);
                return ResponseEntity.noContent().build();
        }

        // ================== OTHER ACTIONS ==================

        @Operation(summary = "Change user role", description = "Change the role of a user")
        @PatchMapping("/{userId}/role")
        public ResponseEntity<UserDto> changeUserRole(
                        @PathVariable Long userId,
                        @RequestParam String newRole,
                        @RequestParam(required = false, defaultValue = "Admin Action") String reason) {
                Long adminId = currentUserProvider.getCurrentUserId();
                User updatedUser = userManagementService.changeUserRole(adminId, userId, newRole, reason);
                return ResponseEntity.ok(UserMapper.toDto(updatedUser));
        }

        @Operation(summary = "Delete user", description = "Permanently delete a user")
        @DeleteMapping("/{userId}")
        public ResponseEntity<Void> deleteUser(
                        @PathVariable Long userId,
                        @RequestParam(required = false, defaultValue = "Admin Action") String reason) {
                Long adminId = currentUserProvider.getCurrentUserId();
                userManagementService.deleteUser(adminId, userId, reason);
                return ResponseEntity.noContent().build();
        }
}
