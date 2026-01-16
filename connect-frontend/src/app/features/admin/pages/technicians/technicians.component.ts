import { Component, inject, OnInit } from '@angular/core';
import { AdminService, Technician, TechnicianSkillDto } from '../../services/admin.service';
import { CommonModule, DatePipe } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-technicians',
  standalone: true,
  imports: [CommonModule, FormsModule, DatePipe],
  templateUrl: './technicians.component.html',
  styleUrl: './technicians.component.css'
})
export class TechniciansComponent implements OnInit {
  private adminService = inject(AdminService);

  technicians: Technician[] = [];
  selectedTechnicianSkills: TechnicianSkillDto[] = [];
  selectedTechnicianName: string | null = null;
  showSkillsModal = false;

  ngOnInit() {
    this.loadTechnicians();
  }

  loadTechnicians() {
    this.adminService.getTechnicians().subscribe(res => {
      // Map backend data to frontend interface if needed
      // Currently using direct map, assuming fields align or are sufficient.
      // Backend Technician extends User entity.
      // Front expects 'specialty' but backend Tech might not have it directly on root if its strictly Entity.
      // Important to note: Backend Entity MIGHT NOT have 'specialty' field exposed if it relies on Category/Skills strictly.
      // But let's assume standard Entity serialization for now.
      this.technicians = res;
    });
  }

  getTechnicianStatus(tech: any): 'ACTIVE' | 'PENDING' | 'REJECTED' {
    if (tech.active) return 'ACTIVE';
    return 'PENDING'; // Default to pending if not active
  }

  approveTechnician(id: number) {
    this.adminService.updateTechnicianStatus(id, 'ACTIVE').subscribe(() => this.loadTechnicians());
  }

  rejectTechnician(id: number) {
    if (confirm('Désactiver ce technicien ?')) {
      this.adminService.updateTechnicianStatus(id, 'REJECTED').subscribe(() => this.loadTechnicians());
    }
  }

  deleteTechnician(id: number) {
    if (confirm('Supprimer définitivement ce technicien ?')) {
      this.adminService.deleteTechnician(id).subscribe(() => this.loadTechnicians());
    }
  }

  viewSkills(tech: Technician) {
    this.selectedTechnicianName = tech.firstName + ' ' + tech.lastName;
    this.adminService.getTechnicianSkills(tech.idUser).subscribe(skills => {
      this.selectedTechnicianSkills = skills;
      this.showSkillsModal = true;
    });
  }

  closeModal() {
    this.showSkillsModal = false;
    this.selectedTechnicianSkills = [];
  }

  verifySkill(skill: TechnicianSkillDto, verified: boolean) {
    this.adminService.verifySkill(skill.idSkill, verified).subscribe(updated => {
      // update local list
      const index = this.selectedTechnicianSkills.findIndex(s => s.idSkill === updated.idSkill);
      if (index !== -1) {
        this.selectedTechnicianSkills[index] = updated;
      }
    });
  }

  getStatusClass(tech: any): string {
    const status = this.getTechnicianStatus(tech);
    switch (status) {
      case 'ACTIVE': return 'bg-green-100 text-green-700';
      case 'PENDING': return 'bg-yellow-100 text-yellow-700';
      case 'REJECTED': return 'bg-red-100 text-red-700';
      default: return 'bg-gray-100 text-gray-700';
    }
  }

  getStatusLabel(tech: any): string {
    const status = this.getTechnicianStatus(tech);
    switch (status) {
      case 'ACTIVE': return 'Actif';
      case 'PENDING': return 'En attente';
      case 'REJECTED': return 'Inactif';
      default: return status;
    }
  }
}
