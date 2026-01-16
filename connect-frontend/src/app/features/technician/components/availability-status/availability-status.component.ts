import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../../../services/auth.service';
import { TechnicianAvailabilityService } from '../../services/technician-availability.service';

type AvailabilityStatus = 'AVAILABLE' | 'BUSY' | 'UNAVAILABLE' | 'ON_BREAK';

@Component({
  selector: 'app-availability-status',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './availability-status.component.html',
  styleUrl: './availability-status.component.css'
})
export class AvailabilityStatusComponent implements OnInit {

  currentStatus: AvailabilityStatus = 'AVAILABLE';
  updating = false;
  successMessage: string | null = null;
  errorMessage: string | null = null;
  technicianId: number | null = null;

  statusLabels: { [key in AvailabilityStatus]: string } = {
    AVAILABLE: '✅ Disponible',
    BUSY: '🔄 Occupé',
    UNAVAILABLE: '❌ Indisponible',
    ON_BREAK: '⏸️ Pause'
  };

  statusDescriptions: { [key in AvailabilityStatus]: string } = {
    AVAILABLE: 'Vous êtes disponible pour accepter les réservations',
    BUSY: 'Vous êtes actuellement occupé par une intervention',
    UNAVAILABLE: 'Vous n\'êtes pas disponible',
    ON_BREAK: 'Vous êtes en pause'
  };

  statusColors: { [key in AvailabilityStatus]: string } = {
    AVAILABLE: 'bg-green-100 text-green-800 border-green-300',
    BUSY: 'bg-orange-100 text-orange-800 border-orange-300',
    UNAVAILABLE: 'bg-red-100 text-red-800 border-red-300',
    ON_BREAK: 'bg-yellow-100 text-yellow-800 border-yellow-300'
  };

  statusButtonColors: { [key in AvailabilityStatus]: string } = {
    AVAILABLE: 'bg-green-600 hover:bg-green-700',
    BUSY: 'bg-orange-600 hover:bg-orange-700',
    UNAVAILABLE: 'bg-red-600 hover:bg-red-700',
    ON_BREAK: 'bg-yellow-600 hover:bg-yellow-700'
  };

  constructor(
    private authService: AuthService,
    private availabilityService: TechnicianAvailabilityService
  ) { }

  ngOnInit(): void {
    const user = this.authService.getUser();
    this.technicianId = user?.userId || null;
    this.loadCurrentStatus();
  }

  loadCurrentStatus(): void {
    if (!this.technicianId) return;

    this.availabilityService.getTechnicianProfile(this.technicianId).subscribe({
      next: (profile) => {
        if (profile?.availabilityStatus) {
          this.currentStatus = profile.availabilityStatus;
        }
      },
      error: (err) => {
        console.error('Erreur lors du chargement du statut', err);
      }
    });
  }

  updateStatus(newStatus: AvailabilityStatus): void {
    if (!this.technicianId) return;

    this.updating = true;
    this.successMessage = null;
    this.errorMessage = null;

    this.availabilityService.updateAvailabilityStatus(this.technicianId, newStatus).subscribe({
      next: () => {
        this.currentStatus = newStatus;
        this.successMessage = `Votre disponibilité est maintenant: ${this.statusLabels[newStatus]}`;
        this.updating = false;
        setTimeout(() => this.successMessage = null, 3000);
      },
      error: (err) => {
        this.errorMessage = err?.error?.message || 'Erreur lors de la mise à jour';
        this.updating = false;
      }
    });
  }

}
