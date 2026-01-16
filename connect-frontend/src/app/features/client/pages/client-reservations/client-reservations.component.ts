import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ReservationControllerService } from '../../../../api/services/reservationController.service';
import { ReservationDto } from '../../../../api/models';
import { ClientHeaderComponent } from "../../../../core/layout/client-header/client-header.component";

@Component({
    selector: 'app-client-reservations',
    standalone: true,
    imports: [CommonModule, RouterModule, ClientHeaderComponent],
    templateUrl: './client-reservations.component.html'
})
export class ClientReservationsComponent implements OnInit {
    reservations: ReservationDto[] = [];
    loading = true;
    activeTab: 'current' | 'history' = 'current';

    constructor(private reservationService: ReservationControllerService) { }

    ngOnInit() {
        this.loadReservations();
    }

    loadReservations() {
        this.loading = true;
        this.reservationService.getMyClientReservations().subscribe({
            next: (data) => {
                console.log('Reservations data:', data);
                if (Array.isArray(data)) {
                    this.reservations = data;
                } else {
                    console.warn('Invalid reservations data format:', data);
                    this.reservations = [];
                }
                this.loading = false;
            },
            error: (err) => {
                console.error('Error loading reservations:', err);
                this.reservations = [];
                this.loading = false;
            }
        });
    }

    get currentReservations() {
        return this.reservations.filter(r => ['PENDING', 'ACCEPTED', 'EN_ROUTE', 'IN_PROGRESS'].includes(r.status || ''));
    }

    get historyReservations() {
        return this.reservations.filter(r => ['COMPLETED', 'CANCELLED', 'REJECTED'].includes(r.status || ''));
    }

    getStatusClass(status?: string): string {
        switch (status) {
            case 'PENDING': return 'bg-yellow-100 text-yellow-800';
            case 'ACCEPTED': return 'bg-blue-100 text-blue-800';
            case 'EN_ROUTE': return 'bg-indigo-100 text-indigo-800';
            case 'IN_PROGRESS': return 'bg-purple-100 text-purple-800';
            case 'COMPLETED': return 'bg-green-100 text-green-800';
            case 'CANCELLED': return 'bg-red-100 text-red-800';
            case 'REJECTED': return 'bg-gray-100 text-gray-800';
            default: return 'bg-gray-100 text-gray-800';
        }
    }

    getStatusLabel(status?: string): string {
        switch (status) {
            case 'PENDING': return 'En attente';
            case 'ACCEPTED': return 'Acceptée';
            case 'EN_ROUTE': return 'En route';
            case 'IN_PROGRESS': return 'En cours';
            case 'COMPLETED': return 'Terminée';
            case 'CANCELLED': return 'Annulée';
            case 'REJECTED': return 'Refusée';
            default: return status || 'Inconnu';
        }
    }
}
