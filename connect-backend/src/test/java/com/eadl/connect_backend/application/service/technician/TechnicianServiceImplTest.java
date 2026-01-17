package com.eadl.connect_backend.application.service.technician;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.eadl.connect_backend.application.dto.TechnicianResultSearchDto;
import com.eadl.connect_backend.domain.model.reservation.ReservationStatus;
import com.eadl.connect_backend.domain.model.technician.AvailabilityStatus;
import com.eadl.connect_backend.domain.model.technician.TechnicianSkill;
import com.eadl.connect_backend.domain.model.user.Technician;
import com.eadl.connect_backend.domain.port.out.persistence.CategoryRepository;
import com.eadl.connect_backend.domain.port.out.persistence.ReservationRepository;
import com.eadl.connect_backend.domain.port.out.persistence.TechnicianSkillRepository;
import com.eadl.connect_backend.domain.port.out.persistence.TechnicianRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class TechnicianServiceImplTest {

    @Mock
    private TechnicianRepository technicianRepository;

    @Mock
    private TechnicianSkillRepository technicianSkillRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ReservationRepository reservationRepository;

    private TechnicianServiceImpl technicianService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        technicianService = new TechnicianServiceImpl(technicianRepository, technicianSkillRepository, categoryRepository, reservationRepository);
    }

    @Test
    public void testSearchByAvailabilityAndPrice() {
        Technician tech = new Technician();
        tech.restore(1L, "John", "Doe", "john@example.com", "", "", null, "CityX", "NeighbourhoodY", null, null, true, false, false, null);

        TechnicianSkill skill = new TechnicianSkill();
        skill.setIdSkill(10L);
        skill.setIdUser(1L);
        skill.setName("Plumbing");
        skill.setAvailabilityStatus(AvailabilityStatus.AVAILABLE);
        skill.setHourlyRate(BigDecimal.valueOf(50));
        skill.setYearsExperience(5);
        skill.setVerified(true);

        when(technicianSkillRepository.findAll()).thenReturn(List.of(skill));
        when(technicianRepository.findById(1L)).thenReturn(Optional.of(tech));
        when(reservationRepository.averageRatingByTechnicianIdAndStatus(anyLong(), org.mockito.Mockito.eq(ReservationStatus.COMPLETED))).thenReturn(4.5);

        List<TechnicianResultSearchDto> results = technicianService.searchTechnicians("CityX", "NeighbourhoodY", null, AvailabilityStatus.AVAILABLE, 4.0, 40.0, 60.0);

        assertEquals(1, results.size());
        TechnicianResultSearchDto dto = results.get(0);
        assertEquals("John Doe", dto.getName());
        assertEquals("Plumbing", dto.getSkillName());
        assertEquals(4.5, dto.getAverageRating());
    }
}
