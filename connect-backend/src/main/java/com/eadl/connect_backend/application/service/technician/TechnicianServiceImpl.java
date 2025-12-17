package com.eadl.connect_backend.application.service.technician;

import java.util.List;
import com.eadl.connect_backend.domain.model.user.Technician;
import com.eadl.connect_backend.domain.model.user.User;
import com.eadl.connect_backend.domain.port.exception.TechnicianNotFoundException;
import com.eadl.connect_backend.domain.port.exception.UserNotFoundException;
import com.eadl.connect_backend.domain.port.in.technician.TechnicianService;
import com.eadl.connect_backend.domain.port.out.persistence.TechnicianRepository;
import com.eadl.connect_backend.domain.port.out.persistence.UserRepository;
import com.eadl.connect_backend.domain.port.out.security.PasswordEncoder;

import jakarta.transaction.Transactional;
import java.util.Optional;

import org.springframework.stereotype.Service;

/**
 * Implémentation du service Technician
 */
@Service
@Transactional
public class TechnicianServiceImpl implements TechnicianService {
    
    private final TechnicianRepository technicianRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    public TechnicianServiceImpl(TechnicianRepository technicianRepository,
                                 UserRepository userRepository,
                                 PasswordEncoder passwordEncoder) {
        this.technicianRepository = technicianRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    @Override
    public Optional<Technician> getTechnicianById(Long idTechnician) {
        return technicianRepository.findById(idTechnician);
    }
    
    @Override
    public Optional<Technician> getTechnicianByUserId(Long idUser) {
        return technicianRepository.findByUserId(idUser);
    }
    
    @Override
    public List<Technician> getAllTechnicians() {
        return technicianRepository.findAll();
    }
    
    @Override
    public List<Technician> getActiveTechnicians() {
        // Récupère les techniciens dont le compte User est actif
        List<Technician> technicians = technicianRepository.findAll();
        
        return technicians.stream()
            .filter(tech -> {
                User user = userRepository.findById(tech.getIdUser())
                    .orElse(null);
                return user != null && user.isActive();
            })
            .toList();
    }
    
    @Override
    public List<Technician> getVerifiedTechnicians() {
        // Récupère les techniciens vérifiés (compte actif)
        return getActiveTechnicians();
    }
    
    @Override
    public Technician createTechnician(String firstName, String lastName, 
                                       String email, String phone, String password) {
        // 1. Vérifier si l'email existe déjà
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Cet email est déjà utilisé");
        }
        
        // 2. Vérifier si le téléphone existe déjà
        if (userRepository.existsByPhone(phone)) {
            throw new IllegalArgumentException("Ce numéro de téléphone est déjà utilisé");
        }
        
        // 3. Encoder le mot de passe
        String encodedPassword = passwordEncoder.encode(password);
        
        // 4. Créer l'utilisateur Technician
        Technician technician = Technician.create(
            firstName, lastName, email, phone, encodedPassword
        );
        
        // 5. Sauvegarder l'utilisateur
        User savedUser = userRepository.save(technician);
        
        // 6. Créer l'entité Technician liée
        Technician technicianEntity = Technician.create(savedUser.getFirstName(), savedUser.getLastName(),savedUser.getEmail(), savedUser.getPhone(), savedUser.getPassword());
        
        return technicianRepository.save(technicianEntity);
    }
    
    @Override
    public Technician updateTechnician(Long idTechnician, String firstName, 
                                       String lastName, String phone) {
        // 1. Récupérer le technicien
        Technician technician = technicianRepository.findById(idTechnician)
            .orElseThrow(() -> new TechnicianNotFoundException(
                "Technicien non trouvé avec l'ID: " + idTechnician));
        
        // 2. Récupérer l'utilisateur associé
        User user = userRepository.findById(technician.getIdUser())
            .orElseThrow(() -> new UserNotFoundException(
                "Utilisateur non trouvé pour le technicien: " + idTechnician));
        
        // 3. Mettre à jour les informations
        user.updateProfile(firstName, lastName, phone);
        
        // 4. Sauvegarder
        userRepository.save(user);
        
        return technician;
    }
    
    @Override
    public Long countTechnicians() {
        return technicianRepository.count();
    }
    
    @Override
    public Long countActiveTechnicians() {
        return (long) getActiveTechnicians().size();
    }
}