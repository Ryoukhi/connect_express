import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReservationsService } from '../../../../api/services/reservations.service';
import { ReservationDto } from '../../../../api/models';
import { RouterModule } from '@angular/router';
import { ReservationControllerService } from '../../../../api/services/reservationController.service';

@Component({
  selector: 'app-pending-reservations',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './pending-reservations.component.html',
  styleUrl: './pending-reservations.component.css'
})
export class PendingReservationsComponent implements OnInit {
  pending: ReservationDto[] = [];
  loading = false;
  error: string | null = null;

  actionInProgress: Set<number> = new Set<number>();
  toastVisible = false;
  toastMessage: string | null = null;
  toastType: 'success' | 'error' | 'info' = 'success';
  private toastTimeoutId: any;

  constructor(private reservationsService: ReservationsService, private reservationController: ReservationControllerService) {}

  ngOnInit(): void {
    this.loadPending();
  }

  loadPending(): void {
    this.loading = true;
    this.error = null;
    this.reservationsService.getMyTechnicianReservations('response').subscribe({
      next: (resp: any) => {
        const body = resp?.body ?? resp;
        const parseJson = (data: any) => {
          if (data instanceof Blob) {
            return new Promise<any>((resolve, reject) => {
              const reader = new FileReader();
              reader.onload = () => {
                try { resolve(JSON.parse(reader.result as string)); } catch (e) { reject(e); }
              };
              reader.onerror = e => reject(e);
              reader.readAsText(data as Blob);
            });
          }
          return Promise.resolve(data);
        };

        Promise.resolve(parseJson(body)).then((arr: ReservationDto[]) => {
          this.pending = (arr || []).map(r => ({...r, scheduledTime: r.scheduledTime ? new Date(r.scheduledTime as any) : undefined}))
            .filter(r => r.status === 'PENDING') as ReservationDto[];
          this.loading = false;
        }).catch(err => {
          console.error(err);
          this.error = 'Erreur parsing réservations';
          this.loading = false;
        });
      },
      error: (err) => {
        console.error(err);
        this.error = 'Impossible de charger les réservations';
        this.loading = false;
      }
    });
  }

  private showToast(message: string, type: 'success' | 'error' | 'info' = 'success', duration = 3000): void {
    this.toastMessage = message;
    this.toastType = type;
    this.toastVisible = true;
    if (this.toastTimeoutId) clearTimeout(this.toastTimeoutId);
    this.toastTimeoutId = setTimeout(() => {
      this.toastVisible = false;
      this.toastMessage = null;
      this.toastTimeoutId = undefined;
    }, duration);
  }

  isActionInProgress(id?: number): boolean {
    return typeof id === 'number' && this.actionInProgress.has(id);
  }

  acceptReservation(r: ReservationDto): void {
    const id = r?.idReservation;
    if (!id) return;
    if (!confirm('Accepter cette réservation ?')) return;
    if (this.actionInProgress.has(id)) return;
    this.actionInProgress.add(id);

    this.reservationController.changeStatus(id, 'ACCEPTED', 'response').subscribe({
      next: () => {
        this.actionInProgress.delete(id);
        // remove from list
        this.pending = this.pending.filter(p => p.idReservation !== id);
        this.showToast('Réservation acceptée', 'success');
      },
      error: (err) => {
        console.error('Accept failed', err);
        this.actionInProgress.delete(id);
        this.showToast("Échec de l'opération", 'error');
      }
    });
  }

  refuseReservation(r: ReservationDto): void {
    const id = r?.idReservation;
    if (!id) return;
    if (!confirm('Refuser cette réservation ?')) return;
    if (this.actionInProgress.has(id)) return;
    this.actionInProgress.add(id);

    this.reservationController.changeStatus(id, 'REJECTED', 'response').subscribe({
      next: () => {
        this.actionInProgress.delete(id);
        this.pending = this.pending.filter(p => p.idReservation !== id);
        this.showToast('Réservation refusée', 'success');
      },
      error: (err) => {
        console.error('Reject failed', err);
        this.actionInProgress.delete(id);
        this.showToast("Échec de l'opération", 'error');
      }
    });
  }
}
