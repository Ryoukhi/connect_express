import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { TechniciensService } from '../../../../api/services/techniciens.service';
import { ReservationControllerService } from '../../../../api/services/reservationController.service';
import { Technician, ReservationDto } from '../../../../api/models';
import { ClientHeaderComponent } from "../../../../core/layout/client-header/client-header.component";

@Component({
    selector: 'app-booking-form',
    standalone: true,
    imports: [CommonModule, FormsModule, ClientHeaderComponent],
    templateUrl: './booking-form.component.html'
})
export class BookingFormComponent implements OnInit {
    technician: Technician | null = null;
    loading = false;
    submitLoading = false;
    errorMsg: string | null = null;
    successMsg: string | null = null;

    reservation: ReservationDto = {
        dateRequested: undefined, // Will be string 'YYYY-MM-DD' from input, needing conversion if DTO expects Date
        scheduledTime: undefined, // 'HH:mm'
        description: '',
        city: '',
        neighborhood: ''
    };

    // UI helpers
    dateInput: string = '';
    timeInput: string = '';

    constructor(
        private route: ActivatedRoute,
        private router: Router,
        private techniciensService: TechniciensService,
        private reservationService: ReservationControllerService
    ) { }

    ngOnInit() {
        const techId = this.route.snapshot.paramMap.get('technicianId');
        if (techId) {
            this.loadTechnician(+techId);
        }
    }

    loadTechnician(id: number) {
        this.loading = true;
        this.techniciensService.getTechnicianById(id).subscribe({
            next: (tech) => {
                this.technician = tech;
                this.loading = false;
                // Pre-fill location if possible
                if (tech.city) this.reservation.city = tech.city;
            },
            error: () => {
                this.errorMsg = "Impossible de charger les infos du technicien.";
                this.loading = false;
            }
        });
    }

    submitBooking() {
        if (!this.dateInput || !this.timeInput || !this.reservation.description) {
            this.errorMsg = "Veuillez remplir tous les champs obligatoires.";
            return;
        }

        if (!this.technician || !this.technician.idUser) {
            this.errorMsg = "Technicien invalide.";
            return;
        }

        this.submitLoading = true;
        this.errorMsg = null;

        // Combine Date and Time
        // Note: Backend DTO expects Date (ISO string normally).
        // Let's assume standard ISO format YYYY-MM-DDTHH:mm:00
        const scheduledDateTime = new Date(`${this.dateInput}T${this.timeInput}:00`);

        const payload: ReservationDto = {
            ...this.reservation,
            idTechnician: this.technician.idUser,
            scheduledTime: scheduledDateTime.toISOString() as any, // Cast to any if Typescript complains about Date vs String mismatch
            dateRequested: new Date().toISOString() as any
        };

        this.reservationService.createReservation(payload).subscribe({
            next: () => {
                this.submitLoading = false;
                this.successMsg = "Réservation envoyée avec succès !";
                setTimeout(() => {
                    this.router.navigate(['/client/reservations']);
                }, 2000);
            },
            error: (err) => {
                this.submitLoading = false;
                this.errorMsg = "Erreur lors de la réservation. Veuillez réessayer.";
                console.error(err);
            }
        });
    }
}
