package com.eadl.connect_backend.application.service.technician;

import java.util.List;
import com.eadl.connect_backend.domain.model.user.Technician;
import com.eadl.connect_backend.domain.model.user.User;
import com.eadl.connect_backend.domain.port.exception.TechnicianNotFoundException;
import com.eadl.connect_backend.domain.port.exception.UserNotFoundException;
import com.eadl.connect_backend.domain.port.in.technician.TechnicianService;
import com.eadl.connect_backend.domain.port.out.persistence.TechnicianRepository;
import com.eadl.connect_backend.domain.port.out.persistence.UserRepository;
import com.eadl.connect_backend.domain.port.out.security.CurrentUserProvider;
import com.eadl.connect_backend.domain.port.out.security.PasswordEncoder;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TechnicianServiceImpl implements TechnicianService {

    private final TechnicianRepository technicianRepository;
    private final CurrentUserProvider currentUserProvider;

    public TechnicianServiceImpl(
            TechnicianRepository technicianRepository,
            CurrentUserProvider currentUserProvider
    ) {
        this.technicianRepository = technicianRepository;
        this.currentUserProvider = currentUserProvider;
    }

    // ===================== CREATE =====================

    @Override
    public Technician registerTechnician(Technician technician) {
        // Au moment de l'enregistrement, le technicien est inactif jusqu'à validation KYC
        technician.deactivate();
        return technicianRepository.save(technician);
    }

    // ===================== BUSINESS =====================

    @Override
    public void validateKyc(Long technicianId) {
        Technician technician = technicianRepository.findById(technicianId)
                .orElseThrow(() -> new UserNotFoundException("Technician not found with ID: " + technicianId));

        technician.activate(); // Activer le compte après validation KYC
        technicianRepository.save(technician);
    }

    // ===================== READ =====================

    @Override
    public List<Technician> getActiveTechnicians() {
        return technicianRepository.findByActiveTrue();
    }

    @Override
    public Long countActiveTechnicians() {
        return getActiveTechnicians().stream().count();
    }

    @Override
    public List<Technician> getTechniciansByCity(String city) {
        // Filtrage basique par ville via profil (si profil contient l'information)
        return technicianRepository.findAll().stream()
                .filter(tech -> tech.getProfile() != null && city.equalsIgnoreCase(tech.getProfile().getCity()))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Technician> getTechnicianById(Long technicianId) {
        return technicianRepository.findById(technicianId);
    }
}