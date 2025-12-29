package com.eadl.connect_backend.application.service.admin;

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
        // Encodage du mot de passe
        admin.changePassword(passwordEncoder.encode(admin.getPassword()));

        // Activation par défaut
        admin.activate();

        return adminRepository.save(admin);
    }

    // ================== USER MANAGEMENT ==================

    @Override
    public void suspendUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id " + userId));

        user.deactivate();
        userRepository.save(user);
    }

    @Override
    public void activateUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id " + userId));

        user.activate();
        userRepository.save(user);
    }
}