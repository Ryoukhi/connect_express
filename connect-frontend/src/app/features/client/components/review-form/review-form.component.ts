import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ReviewControllerService } from '../../../../api/services/reviewController.service';
import { ReviewDto } from '../../../../api/models';

@Component({
    selector: 'app-review-form',
    standalone: true,
    imports: [CommonModule, FormsModule],
    templateUrl: './review-form.component.html'
})
export class ReviewFormComponent {
    @Input() reservationId: number | undefined;
    @Input() existingReview: ReviewDto | null = null;
    @Output() onClose = new EventEmitter<void>();
    @Output() onSubmitSuccess = new EventEmitter<void>();

    rating = 0;
    comment = '';
    loading = false;
    hoverRating = 0;

    constructor(private reviewService: ReviewControllerService) { }

    ngOnInit() {
        if (this.existingReview) {
            this.rating = this.existingReview.rating?.value || 0;
            this.comment = this.existingReview.comment || '';
        }
    }

    submitReview() {
        if (this.rating === 0) return;

        this.loading = true;

        if (this.existingReview?.idReview) {
            // Update existing
            const review: ReviewDto = {
                ...this.existingReview,
                rating: { value: this.rating },
                comment: this.comment
            };
            this.reviewService.updateReview(this.existingReview.idReview, review).subscribe({
                next: () => {
                    this.loading = false;
                    this.onSubmitSuccess.emit();
                },
                error: (err) => {
                    console.error(err);
                    this.loading = false;
                    alert("Erreur lors de la mise à jour de l'avis.");
                }
            });
        } else {
            // Create new
            const review: ReviewDto = {
                idReservation: this.reservationId,
                rating: { value: this.rating },
                comment: this.comment
            };

            this.reviewService.createReview(review).subscribe({
                next: () => {
                    this.loading = false;
                    this.onSubmitSuccess.emit();
                },
                error: (err) => {
                    console.error(err);
                    this.loading = false;
                    alert("Erreur lors de l'envoi de l'avis.");
                }
            });
        }
    }
}
