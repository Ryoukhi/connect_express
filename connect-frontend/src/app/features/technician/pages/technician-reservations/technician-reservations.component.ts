import { ReservationDto, UserDto, ReviewDto } from '../../../../api/models';
import { TechnicianReservationService } from '../../services/technician-reservation.service';
import { UserControllerService } from '../../../../api/services/userController.service';
import { ReviewControllerService } from '../../../../api/services/reviewController.service';
import { HeaderDashboardTechComponent } from "../../components/header-dashboard-tech/header-dashboard-tech.component";
import { SidebarDashboardTechComponent } from "../../components/sidebar-dashboard-tech/sidebar-dashboard-tech.component";
import { NavDashboardTechComponent } from "../../components/nav-dashboard-tech/nav-dashboard-tech.component";
import { FormsModule } from '@angular/forms';
import { forkJoin, of } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-technician-reservations',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    HeaderDashboardTechComponent,
    SidebarDashboardTechComponent,
    NavDashboardTechComponent
  ],
  templateUrl: './technician-reservations.component.html',
  styleUrl: './technician-reservations.component.css'
})
export class TechnicianReservationsComponent implements OnInit {

  reservations: ReservationDto[] = [];
  filteredReservations: ReservationDto[] = [];
  selectedReservation: ReservationDto | null = null;
  selectedClient: UserDto | null = null; // To store client info
  selectedReview: ReviewDto | null = null; // To store review info
  loading = false;
  actionInProgress = false;
  errorMessage: string | null = null;
  successMessage: string | null = null;
  searchTerm = '';

  // Filtres
  statusFilter = 'ALL'; // ALL, PENDING, ACCEPTED, EN_ROUTE, IN_PROGRESS, COMPLETED, REJECTED, CANCELLED

  // Status labels
  statusLabels: { [key: string]: string } = {
    PENDING: 'En attente',
    ACCEPTED: 'Acceptée',
    EN_ROUTE: 'En route',
    IN_PROGRESS: 'En cours',
    COMPLETED: 'Complétée',
    REJECTED: 'Rejetée',
    CANCELLED: 'Annulée'
  };

  statusColors: { [key: string]: string } = {
    PENDING: 'bg-yellow-100 text-yellow-800',
    ACCEPTED: 'bg-blue-100 text-blue-800',
    EN_ROUTE: 'bg-purple-100 text-purple-800',
    IN_PROGRESS: 'bg-orange-100 text-orange-800',
    COMPLETED: 'bg-green-100 text-green-800',
    REJECTED: 'bg-red-100 text-red-800',
    CANCELLED: 'bg-gray-100 text-gray-800'
  };

  constructor(
    private reservationService: TechnicianReservationService,
    private userService: UserControllerService,
    private reviewService: ReviewControllerService
  ) { }

  ngOnInit(): void {
    this.loadReservations();
  }

  loadReservations(refresh = false): void {
    if (!refresh) this.loading = true;
    this.errorMessage = null;

    this.reservationService.getMyReservations().subscribe({
      next: (data) => {
        this.reservations = data || [];
        // Sort by date (descending)
        this.reservations.sort((a, b) => {
          const dateA = a.scheduledTime ? new Date(a.scheduledTime).getTime() : 0;
          const dateB = b.scheduledTime ? new Date(b.scheduledTime).getTime() : 0;
          return dateB - dateA;
        });
        this.applyFilter();
        this.loading = false;

        // If we had a selected reservation, update it
        if (this.selectedReservation) {
          const updated = this.reservations.find(r => r.idReservation === this.selectedReservation?.idReservation);
          if (updated) this.selectedReservation = updated;
        }
      },
      error: (err) => {
        this.errorMessage = 'Erreur lors du chargement des réservations';
        this.loading = false;
        console.error(err);
      }
    });
  }

  applyFilter(): void {
    let filtered = this.reservations;

    if (this.statusFilter !== 'ALL') {
      filtered = filtered.filter(r => r.status === this.statusFilter);
    }

    if (this.searchTerm) {
      const search = this.searchTerm.toLowerCase();
      filtered = filtered.filter(r =>
        (r.description?.toLowerCase().includes(search)) ||
        (r.city?.toLowerCase().includes(search)) ||
        (r.neighborhood?.toLowerCase().includes(search))
      );
    }

    this.filteredReservations = filtered;
  }

  selectReservation(reservation: ReservationDto): void {
    this.selectedReservation = reservation;
    this.selectedClient = null;
    this.selectedReview = null;
    this.errorMessage = null;
    this.successMessage = null;

    // Fetch client info if we have idClient
    if (reservation.idClient) {
      this.userService.getUserById(reservation.idClient).subscribe({
        next: (user: UserDto) => this.selectedClient = user,
        error: () => console.warn('Could not load client details')
      });
    }

    // Fetch review if it exists
    if (reservation.idReservation && (reservation.status === 'COMPLETED' || reservation.idReview)) {
      this.reviewService.getReviewForReservation(reservation.idReservation).subscribe({
        next: (review) => this.selectedReview = review,
        error: () => console.warn('No review found for this reservation')
      });
    }
  }

  closeDetail(): void {
    this.selectedReservation = null;
    this.errorMessage = null;
    this.successMessage = null;
  }

  acceptReservation(): void {
    if (!this.selectedReservation?.idReservation) return;

    this.actionInProgress = true;
    this.errorMessage = null;

    this.reservationService.acceptReservation(this.selectedReservation.idReservation).subscribe({
      next: (updated) => {
        this.successMessage = 'Réservation acceptée';
        this.loadReservations();
        this.selectedReservation = updated;
        this.actionInProgress = false;
      },
      error: (err) => {
        this.errorMessage = err?.error?.message || 'Erreur lors de l\'acceptation';
        this.actionInProgress = false;
      }
    });
  }

  rejectReservation(): void {
    if (!this.selectedReservation?.idReservation) return;

    const reason = prompt('Raison du rejet:');
    if (!reason) return;

    this.actionInProgress = true;
    this.errorMessage = null;

    this.reservationService.rejectReservation(this.selectedReservation.idReservation, reason).subscribe({
      next: () => {
        this.successMessage = 'Réservation rejetée';
        this.loadReservations();
        this.selectedReservation = null;
        this.actionInProgress = false;
      },
      error: (err) => {
        this.errorMessage = err?.error?.message || 'Erreur lors du rejet';
        this.actionInProgress = false;
      }
    });
  }

  startRoute(): void {
    if (!this.selectedReservation?.idReservation) return;

    this.actionInProgress = true;
    this.errorMessage = null;

    this.reservationService.startRoute(this.selectedReservation.idReservation).subscribe({
      next: (updated) => {
        this.successMessage = 'Statut mis à jour: En route';
        this.loadReservations();
        this.selectedReservation = updated;
        this.actionInProgress = false;
      },
      error: (err) => {
        this.errorMessage = err?.error?.message || 'Erreur lors de la mise à jour';
        this.actionInProgress = false;
      }
    });
  }

  startWork(): void {
    if (!this.selectedReservation?.idReservation) return;

    this.actionInProgress = true;
    this.errorMessage = null;

    this.reservationService.startWork(this.selectedReservation.idReservation).subscribe({
      next: (updated) => {
        this.successMessage = 'Statut mis à jour: En cours';
        this.loadReservations();
        this.selectedReservation = updated;
        this.actionInProgress = false;
      },
      error: (err) => {
        this.errorMessage = err?.error?.message || 'Erreur lors de la mise à jour';
        this.actionInProgress = false;
      }
    });
  }

  completeWork(): void {
    if (!this.selectedReservation?.idReservation) return;

    if (!confirm('Marquer cette réservation comme complétée?')) return;

    this.actionInProgress = true;
    this.errorMessage = null;

    this.reservationService.completeReservation(this.selectedReservation.idReservation).subscribe({
      next: (updated) => {
        this.successMessage = 'Réservation complétée';
        this.loadReservations();
        this.selectedReservation = updated;
        this.actionInProgress = false;
      },
      error: (err) => {
        this.errorMessage = err?.error?.message || 'Erreur lors de la complétion';
        this.actionInProgress = false;
      }
    });
  }

  cancelReservation(): void {
    if (!this.selectedReservation?.idReservation) return;

    const reason = prompt('Raison de l\'annulation:');
    if (!reason) return;

    this.actionInProgress = true;
    this.errorMessage = null;

    this.reservationService.cancelReservation(this.selectedReservation.idReservation, reason).subscribe({
      next: () => {
        this.successMessage = 'Réservation annulée';
        this.loadReservations();
        this.selectedReservation = null;
        this.actionInProgress = false;
      },
      error: (err) => {
        this.errorMessage = err?.error?.message || 'Erreur lors de l\'annulation';
        this.actionInProgress = false;
      }
    });
  }

  getActionButtons(reservation: ReservationDto): string[] {
    const status = reservation.status;
    const buttons: string[] = [];

    if (status === 'PENDING') {
      buttons.push('accept', 'reject');
    } else if (status === 'ACCEPTED') {
      buttons.push('start-route', 'cancel');
    } else if (status === 'EN_ROUTE') {
      buttons.push('start-work', 'cancel');
    } else if (status === 'IN_PROGRESS') {
      buttons.push('complete');
    }

    return buttons;
  }

  isPendingOrAccepted(reservation: ReservationDto): boolean {
    return reservation.status === 'PENDING' || reservation.status === 'ACCEPTED';
  }

}
