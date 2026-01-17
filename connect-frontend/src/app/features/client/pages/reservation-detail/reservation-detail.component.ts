import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { ReservationControllerService } from '../../../../api/services/reservationController.service';
import { ReviewControllerService } from '../../../../api/services/reviewController.service';
import { ReservationDto, ReviewDto } from '../../../../api/models';
import { ReviewFormComponent } from '../../components/review-form/review-form.component';
import { ClientHeaderComponent } from "../../../../core/layout/client-header/client-header.component";

@Component({
    selector: 'app-reservation-detail',
    standalone: true,
    imports: [CommonModule, ReviewFormComponent, ClientHeaderComponent],
    templateUrl: './reservation-detail.component.html'
})
export class ReservationDetailComponent implements OnInit {
    reservation: ReservationDto | null = null;
    existingReview: ReviewDto | null = null;
    loading = true;
    errorMsg: string | null = null;
    showReviewForm = false;

    constructor(
        private route: ActivatedRoute,
        private router: Router,
        private reservationService: ReservationControllerService,
        private reviewService: ReviewControllerService
    ) { }

    ngOnInit() {
        const id = this.route.snapshot.paramMap.get('id');
        if (id) {
            this.loadReservation(+id);
        }
    }

    loadReservation(id: number) {
        this.loading = true;
        this.reservationService.getById(id).subscribe({
            next: (res) => {
                this.reservation = res;
                this.loading = false;

                // Fetch review if exists
                if (res.idReservation && (res.status === 'COMPLETED' || res.idReview)) {
                    this.reviewService.getReviewForReservation(res.idReservation).subscribe({
                        next: (review) => this.existingReview = review,
                        error: () => this.existingReview = null
                    });
                }
            },
            error: () => {
                this.errorMsg = "Réservation introuvable.";
                this.loading = false;
            }
        });
    }

    cancelReservation() {
        if (!this.reservation?.idReservation) return;
        if (!confirm('Êtes-vous sûr de vouloir annuler cette réservation ?')) return;

        this.reservationService.cancelReservation(this.reservation.idReservation, 'Annulé par le client').subscribe({
            next: (updated) => {
                this.reservation = updated;
            },
            error: () => {
                alert("Impossible d'annuler la réservation.");
            }
        });
    }

    openReview() {
        this.showReviewForm = true;
    }

    onReviewSubmitted() {
        this.showReviewForm = false;
        // Reload to potentially show review data if we were to display it
        if (this.reservation?.idReservation) {
            this.loadReservation(this.reservation.idReservation);
        }
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
}
