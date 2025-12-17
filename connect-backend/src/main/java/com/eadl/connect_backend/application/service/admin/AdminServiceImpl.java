package com.eadl.connect_backend.application.service.admin;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eadl.connect_backend.domain.model.user.Admin;
import com.eadl.connect_backend.domain.port.exception.AdminNotFoundException;
import com.eadl.connect_backend.domain.port.in.admin.AdminService;
import com.eadl.connect_backend.domain.port.out.persistence.AdminRepository;
import com.eadl.connect_backend.domain.port.out.persistence.UserRepository;
import com.eadl.connect_backend.domain.port.out.security.PasswordEncoder;

import java.util.Optional;

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
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Admin> getAdminById(Long idAdmin) {
        return adminRepository.findById(idAdmin);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Admin> getAdminByEmail(String email) {
        return userRepository.findByEmail(email)
            .flatMap(user -> adminRepository.findByUserId(user.getIdUser()));
    }
    
    @Override
    public Admin createAdmin(String firstName, String lastName, String email, 
                            String phone, String password) {
        // Vérifier si l'email existe déjà
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Cet email est déjà utilisé");
        }
        
        // Encoder le mot de passe
        String encodedPassword = passwordEncoder.encode(password);
        
        // Créer l'admin
        Admin admin = Admin.create(firstName, lastName, email, phone, encodedPassword);
        
        return (Admin) userRepository.save(admin);
    }
    
    @Override
    public Admin updateAdminProfile(Long idAdmin, String firstName, 
                                   String lastName, String phone) {
        Admin admin = adminRepository.findById(idAdmin)
            .orElseThrow(() -> new AdminNotFoundException("Admin non trouvé"));
        
        admin.updateProfile(firstName, lastName, phone);
        
        return (Admin) userRepository.save(admin);
    }
    
    @Override
    public void changePassword(Long idAdmin, String oldPassword, String newPassword) {
        Admin admin = adminRepository.findById(idAdmin)
            .orElseThrow(() -> new AdminNotFoundException("Admin non trouvé"));
        
        // Vérifier l'ancien mot de passe
        if (!passwordEncoder.matches(oldPassword, admin.getPassword())) {
            throw new IllegalArgumentException("Ancien mot de passe incorrect");
        }
        
        // Encoder et changer le mot de passe
        String encodedPassword = passwordEncoder.encode(newPassword);
        admin.changePassword(encodedPassword);
        
        userRepository.save(admin);
    }
}