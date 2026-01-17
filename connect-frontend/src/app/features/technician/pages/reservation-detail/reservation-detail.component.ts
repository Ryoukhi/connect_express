import { Component, OnInit } from '@angular/core';
import { CommonModule, Location } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { ReservationsService } from '../../../../api/services/reservations.service';
import { ReservationDto } from '../../../../api/models';

@Component({
  selector: 'app-reservation-detail',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './reservation-detail.component.html',
  styleUrl: './reservation-detail.component.css'
})
export class ReservationDetailComponent implements OnInit {
  reservation: ReservationDto | null = null;
  loading = false;
  error: string | null = null;

  constructor(private route: ActivatedRoute, private reservationsService: ReservationsService, private location: Location) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const id = Number(params.get('id'));
      if (!id) { this.error = 'Identifiant invalide'; return; }
      this.loadReservation(id);
    });
  }

  private loadReservation(id: number): void {
    this.loading = true;
    this.error = null;
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
          this.reservation = ({ ...res, scheduledTime: res.scheduledTime ? new Date(res.scheduledTime as any) : undefined } as ReservationDto);
          this.loading = false;
        }).catch(err => {
          console.error(err);
          this.error = 'Erreur lors du parsing de la réservation';
          this.loading = false;
        });
      },
      error: (err) => {
        console.error(err);
        this.error = 'Impossible de charger la réservation';
        this.loading = false;
      }
    });
  }

  goBack() {
    this.location.back();
  }
}
