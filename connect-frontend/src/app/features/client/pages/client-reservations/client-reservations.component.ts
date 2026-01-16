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
        console.log('Fetching client reservations...');
        this.reservationService.getMyClientReservations().subscribe({
            next: (data) => {
                console.log('Successfully loaded reservations:', data);
                // Ensure data is an array and filter out any invalid entries
                if (data && Array.isArray(data)) {
                    this.reservations = data.sort((a, b) => {
                        // Sort by most recent first
                        const dateA = a.createdAt ? new Date(a.createdAt).getTime() : 0;
                        const dateB = b.createdAt ? new Date(b.createdAt).getTime() : 0;
                        return dateB - dateA;
                    });
                } else {
                    console.error('Expected array for reservations but received:', typeof data);
                    this.reservations = [];
                }
                this.loading = false;
            },
            error: (err) => {
                console.error('Failed to load reservations:', err);
                this.reservations = [];
                this.loading = false;
            }
        });
    }

    get currentReservations() {
        const ongoingStatuses = ['PENDING', 'ACCEPTED', 'EN_ROUTE', 'IN_PROGRESS'];
        return this.reservations.filter(r => ongoingStatuses.includes(r.status || ''));
    }

    get historyReservations() {
        const historyStatuses = ['COMPLETED', 'CANCELLED', 'REJECTED'];
        return this.reservations.filter(r => historyStatuses.includes(r.status || ''));
    }

    getStatusClass(status?: string): string {
        switch (status) {
            case 'PENDING':
                return 'bg-amber-100 text-amber-700 border-amber-200';
            case 'ACCEPTED':
                return 'bg-blue-100 text-blue-700 border-blue-200';
            case 'EN_ROUTE':
                return 'bg-sky-100 text-sky-700 border-sky-200';
            case 'IN_PROGRESS':
                return 'bg-indigo-100 text-indigo-700 border-indigo-200';
            case 'COMPLETED':
                return 'bg-emerald-100 text-emerald-700 border-emerald-200';
            case 'CANCELLED':
                return 'bg-rose-100 text-rose-700 border-rose-200';
            case 'REJECTED':
                return 'bg-slate-100 text-slate-700 border-slate-200';
            default:
                return 'bg-slate-100 text-slate-500 border-slate-200';
        }
    }

    getStatusLabel(status?: string): string {
        const labels: { [key: string]: string } = {
            'PENDING': 'En attente',
            'ACCEPTED': 'Confirmée',
            'EN_ROUTE': 'En chemin',
            'IN_PROGRESS': 'En intervention',
            'COMPLETED': 'Terminée',
            'CANCELLED': 'Annulée',
            'REJECTED': 'Refusée'
        };
        return labels[status || ''] || status || 'Inconnu';
    }
}
