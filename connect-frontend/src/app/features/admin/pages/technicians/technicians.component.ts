import { Component, inject, OnInit } from '@angular/core';
import { AdminService, Technician } from '../../services/admin.service';
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

  ngOnInit() {
    this.loadTechnicians();
  }

  loadTechnicians() {
    this.adminService.getTechnicians().subscribe(res => {
      this.technicians = res;
    });
  }

  approveTechnician(id: number) {
    this.adminService.updateTechnicianStatus(id, 'ACTIVE').subscribe(() => this.loadTechnicians());
  }

  rejectTechnician(id: number) {
    if (confirm('Refuser ce technicien ?')) {
      this.adminService.updateTechnicianStatus(id, 'REJECTED').subscribe(() => this.loadTechnicians());
    }
  }

  deleteTechnician(id: number) {
    if (confirm('Supprimer définitivement ce technicien ?')) {
      this.adminService.deleteTechnician(id).subscribe(() => this.loadTechnicians());
    }
  }

  getStatusClass(status: string): string {
    switch (status) {
      case 'ACTIVE': return 'bg-green-100 text-green-700';
      case 'PENDING': return 'bg-yellow-100 text-yellow-700';
      case 'REJECTED': return 'bg-red-100 text-red-700';
      default: return 'bg-gray-100 text-gray-700';
    }
  }

  getStatusLabel(status: string): string {
    switch (status) {
      case 'ACTIVE': return 'Actif';
      case 'PENDING': return 'En attente';
      case 'REJECTED': return 'Refusé';
      default: return status;
    }
  }
}
