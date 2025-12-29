package com.eadl.connect_backend.application.service.technician;

import java.util.List;
import com.eadl.connect_backend.domain.model.user.Technician;
import com.eadl.connect_backend.domain.port.in.technician.TechnicianService;
import com.eadl.connect_backend.domain.port.out.persistence.TechnicianRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class TechnicianServiceImpl implements TechnicianService {

    private final TechnicianRepository technicianRepository;

    public TechnicianServiceImpl(TechnicianRepository technicianRepository) {
        this.technicianRepository = technicianRepository;
    }

    @Override
    public Technician registerTechnician(Technician technician) {
     
        return technicianRepository.save(technician);
    }

    @Override
    public void validateKyc(Long technicianId) {
        Optional<Technician> optTech = technicianRepository.findById(technicianId);
        if (optTech.isPresent()) {
            Technician tech = optTech.get();
            tech.activate();
            technicianRepository.save(tech);
        } else {
            throw new IllegalArgumentException("Technician not found with id " + technicianId);
        }
    }

    @Override
    public List<Technician> getActiveTechnicians() {
        return technicianRepository.findAll().stream()
                .filter(Technician::isActive)
                .collect(Collectors.toList());
    }

    @Override
    public Long countActiveTechnicians() {
        return technicianRepository.findAll().stream()
                .filter(Technician::isActive)
                .count();
    }

    @Override
    public List<Technician> getTechniciansByCity(String city) {
        return technicianRepository.findAll().stream()
                .filter(tech -> tech.getCity() != null && city.equalsIgnoreCase(tech.getCity()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Technician> getTechniciansByNeighborhood(String neighborhood) {
        return technicianRepository.findAll().stream()
                .filter(tech -> tech.getNeighborhood() != null && neighborhood.equalsIgnoreCase(tech.getNeighborhood()))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Technician> getTechnicianById(Long technicianId) {
        return technicianRepository.findById(technicianId);
    }
}