import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ReservationsService } from '../../../../api/services/reservations.service';
import { ReservationDto } from '../../../../api/models';

@Component({
  selector: 'app-list-reservations',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './list-reservations.component.html',
  styleUrl: './list-reservations.component.css'
})
export class ListReservationsComponent implements OnInit {
  reservations: ReservationDto[] = [];
  loading = false;
  error: string | null = null;

  // details state
  selected: ReservationDto | null = null;
  selectedLoading = false;
  selectedError: string | null = null;

  constructor(private reservationsService: ReservationsService) {}

  ngOnInit(): void {
    this.loadTodaysConfirmed();
  }

  private loadTodaysConfirmed(): void {
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
          const today = new Date();
          const isSameDay = (d1: Date, d2: Date) => d1.getFullYear() === d2.getFullYear() && d1.getMonth() === d2.getMonth() && d1.getDate() === d2.getDate();

          const reservations = (arr || []).map(r => ({...r, scheduledTime: r.scheduledTime ? new Date(r.scheduledTime as any) : undefined}));

          // Filter confirmed reservations for today (status === 'ACCEPTED')
          this.reservations = reservations.filter(r => r.scheduledTime && isSameDay(r.scheduledTime as Date, today) && r.status === 'ACCEPTED') as ReservationDto[];

          this.loading = false;
        }).catch(err => {
          this.error = 'Erreur lors du traitement des réservations';
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

  viewDetails(r: ReservationDto) {
    const id = r?.idReservation;
    if (!id) return;
    this.selected = null;
    this.selectedLoading = true;
    this.selectedError = null;

    this.reservationsService.getById(id, 'response').subscribe({
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

        Promise.resolve(parseJson(body)).then((res: ReservationDto) => {
          this.selected = ({ ...res, scheduledTime: res.scheduledTime ? new Date(res.scheduledTime as any) : undefined } as ReservationDto);
          this.selectedLoading = false;
        }).catch(err => {
          console.error(err);
          this.selectedError = 'Erreur parsing détails';
          this.selectedLoading = false;
        });
      },
      error: (err) => {
        console.error(err);
        this.selectedError = 'Impossible de charger les détails';
        this.selectedLoading = false;
      }
    });
  }

  closeDetails() {
    this.selected = null;
    this.selectedError = null;
    this.selectedLoading = false;
  }

}
