package com.eadl.connect_backend.application.service.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eadl.connect_backend.domain.model.user.Admin;
import com.eadl.connect_backend.domain.model.user.User;
import com.eadl.connect_backend.domain.port.exception.UserNotFoundException;
import com.eadl.connect_backend.domain.port.in.admin.AdminService;
import com.eadl.connect_backend.domain.port.out.persistence.AdminRepository;
import com.eadl.connect_backend.domain.port.out.persistence.UserRepository;
import com.eadl.connect_backend.domain.port.out.security.PasswordEncoder;

@Service
@Transactional
public class AdminServiceImpl implements AdminService {

    private static final Logger log = LoggerFactory.getLogger(AdminServiceImpl.class);

    private final AdminRepository adminRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminServiceImpl(AdminRepository adminRepository,
                            UserRepository userRepository,
                            PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ================== CREATE ADMIN ==================

    @Override
    public Admin createAdmin(Admin admin) {
        log.info("Creating new admin with email={}", admin.getEmail());

        admin.changePassword(passwordEncoder.encode(admin.getPassword()));
        admin.activate();

        Admin savedAdmin = adminRepository.save(admin);

        log.info("Admin created successfully with id={}", savedAdmin.getId());

        return savedAdmin;
    }

    // ================== USER MANAGEMENT ==================

    @Override
    public void suspendUser(Long userId) {
        log.info("Suspending user with id={}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("User not found, cannot suspend userId={}", userId);
                    return new UserNotFoundException("User not found with id " + userId);
                });

        user.deactivate();
        userRepository.save(user);

        log.info("User suspended successfully, userId={}", userId);
    }

    @Override
    public void activateUser(Long userId) {
        log.info("Activating user with id={}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("User not found, cannot activate userId={}", userId);
                    return new UserNotFoundException("User not found with id " + userId);
                });

        user.activate();
        userRepository.save(user);

        log.info("User activated successfully, userId={}", userId);
    }
}
