import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { ReservationControllerService } from '../../../../api/services/reservationController.service';
import { ReservationDto } from '../../../../api/models';
import { ReservationsService } from '../../../../api/services/reservations.service';
import { AuthService } from '../../../../services/auth.service';

@Component({
  selector: 'app-main-dashboard-tech',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './main-dashboard-tech.component.html',
  styleUrl: './main-dashboard-tech.component.css'
})
export class MainDashboardTechComponent implements OnInit {
  allReservations: ReservationDto[] = [];
  todaysReservations: ReservationDto[] = [];
  todaysCount = 0;
  pendingCount = 0;
  loading = false;
  error: string | null = null;
  showAll = false;

  revenue: number | null = null;
  revenueLoading = false;
  revenueError: string | null = null;

  rating: number | null = null;
  ratingLoading = false;
  ratingError: string | null = null;

  pendingRecent: ReservationDto[] = [];
  lastTwoReservations: ReservationDto[] = [];
  actionInProgress: Set<number> = new Set<number>();

  // details modal state
  selectedReservation: ReservationDto | null = null;
  selectedReservationLoading = false;
  selectedReservationError: string | null = null;

  // in-component toast
  toastVisible = false;
  toastMessage: string | null = null;
  toastType: 'success' | 'error' | 'info' = 'success';
  private toastTimeoutId: any;

  showToast(message: string, type: 'success' | 'error' | 'info' = 'success', duration = 3000): void {
    this.toastMessage = message;
    this.toastType = type;
    this.toastVisible = true;
    if (this.toastTimeoutId) {
      clearTimeout(this.toastTimeoutId);
    }
    this.toastTimeoutId = setTimeout(() => {
      this.toastVisible = false;
      this.toastMessage = null;
      this.toastTimeoutId = undefined;
    }, duration);
  }

  openDetails(r: ReservationDto): void {
    const id = r?.idReservation;
    if (!id) return;
    this.selectedReservationLoading = true;
    this.selectedReservationError = null;
    // attempt to fetch fresh details from backend
    this.reservationService.getById(id, 'response').subscribe({
      next: (resp: any) => {
        const body = resp?.body ?? resp;
        // parse possible Blob
        const parseJson = (data: any) => {
          if (data instanceof Blob) {
            return new Promise<any>((resolve, reject) => {
              const reader = new FileReader();
              reader.onload = () => {
                try {
                  resolve(JSON.parse(reader.result as string));
                } catch (err) {
                  reject(err);
                }
              };
              reader.onerror = (e) => reject(e);
              reader.readAsText(data as Blob);
            });
          }
          return Promise.resolve(data);
        };

        Promise.resolve(parseJson(body)).then((res: ReservationDto) => {
          this.selectedReservation = ({ ...res, scheduledTime: res.scheduledTime ? new Date(res.scheduledTime as any) : undefined } as ReservationDto);
          this.selectedReservationLoading = false;
        }).catch(err => {
          console.error('Failed to parse reservation details', err);
          this.selectedReservationError = 'Erreur lecture détails';
          this.selectedReservationLoading = false;
        });
      },
      error: (err) => {
        console.error('Failed to fetch reservation details', err);
        this.selectedReservationError = 'Impossible de charger les détails';
        this.selectedReservationLoading = false;
      }
    });
  }

  closeDetails(): void {
    this.selectedReservation = null;
    this.selectedReservationError = null;
    this.selectedReservationLoading = false;
  }

  constructor(
    private reservationService: ReservationControllerService,
    private reservationsStatsService: ReservationsService,
    private authService: AuthService
  ) {}

  acceptReservation(r: ReservationDto): void {
    const id = r?.idReservation;
    if (!id) return;
    if (this.actionInProgress.has(id)) return;
    this.actionInProgress.add(id);

    this.reservationService.changeStatus(id, 'ACCEPTED', 'response').subscribe({
      next: () => {
        this.actionInProgress.delete(id);
        // refresh data
        this.loadTodaysReservations();
        this.showToast('Réservation acceptée', 'success');
        if (this.selectedReservation?.idReservation === id) {
          this.selectedReservation.status = 'ACCEPTED';
          setTimeout(() => this.closeDetails(), 700);
        }
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
    if (this.actionInProgress.has(id)) return;
    this.actionInProgress.add(id);

    this.reservationService.changeStatus(id, 'REJECTED', 'response').subscribe({
      next: () => {
        this.actionInProgress.delete(id);
        // refresh data
        this.loadTodaysReservations();
        this.showToast('Réservation refusée', 'success');
        if (this.selectedReservation?.idReservation === id) {
          this.selectedReservation.status = 'REJECTED';
          setTimeout(() => this.closeDetails(), 700);
        }
      },
      error: (err) => {
        console.error('Reject failed', err);
        this.actionInProgress.delete(id);
        this.showToast("Échec de l'opération", 'error');
      }
    });
  }

  isActionInProgress(id?: number): boolean {
    return typeof id === 'number' && this.actionInProgress.has(id);
  }

  ngOnInit(): void {
    this.loadTodaysReservations();
    this.loadRevenue();
    this.loadRating();
  }

  private loadTodaysReservations(): void {
    this.loading = true;
    this.error = null;

    // The generated service returns a blob; request the full response and parse it robustly
    this.reservationService.getMyTechnicianReservations('response').subscribe({
      next: (resp: any) => {
        try {
          const body = resp?.body ?? resp;

          const parseJson = (data: any) => {
            if (data instanceof Blob) {
              return new Promise<any>((resolve, reject) => {
                const reader = new FileReader();
                reader.onload = () => {
                  try {
                    resolve(JSON.parse(reader.result as string));
                  } catch (err) {
                    reject(err);
                  }
                };
                reader.onerror = (e) => reject(e);
                reader.readAsText(data as Blob);
              });
            }

            // already JSON
            return Promise.resolve(data);
          };

          Promise.resolve(parseJson(body)).then((arr: ReservationDto[]) => {
            const today = new Date();
            const isSameDay = (d1: Date, d2: Date) => d1.getFullYear() === d2.getFullYear() && d1.getMonth() === d2.getMonth() && d1.getDate() === d2.getDate();

            const reservations = (arr || []).map(r => ({...r, scheduledTime: r.scheduledTime ? new Date(r.scheduledTime as any) : undefined}));
            // store all reservations for other stats
            this.allReservations = reservations as ReservationDto[];

            // compute today's reservations
            this.todaysReservations = (reservations.filter(r => r.scheduledTime && isSameDay(r.scheduledTime as Date, today)) as unknown) as ReservationDto[];
            this.todaysCount = this.todaysReservations.length;

            // compute pending reservations for the technician
            const pending = this.allReservations.filter(r => r.status === 'PENDING');
            this.pendingCount = pending.length;

            // sort by dateRequested (newest first) then scheduledTime as fallback
            pending.sort((a, b) => {
              const aDate = a.dateRequested ? new Date(a.dateRequested as any).getTime() : (a.scheduledTime ? new Date(a.scheduledTime as any).getTime() : 0);
              const bDate = b.dateRequested ? new Date(b.dateRequested as any).getTime() : (b.scheduledTime ? new Date(b.scheduledTime as any).getTime() : 0);
              return bDate - aDate;
            });

            this.pendingRecent = pending.slice(0, 2) as ReservationDto[];

            // compute last two reservations overall (by dateRequested desc, fallback scheduledTime)
            const sortedAll = [...this.allReservations].sort((a, b) => {
              const aDate = a.dateRequested ? new Date(a.dateRequested as any).getTime() : (a.scheduledTime ? new Date(a.scheduledTime as any).getTime() : 0);
              const bDate = b.dateRequested ? new Date(b.dateRequested as any).getTime() : (b.scheduledTime ? new Date(b.scheduledTime as any).getTime() : 0);
              return bDate - aDate;
            });
            this.lastTwoReservations = sortedAll.slice(0, 2) as ReservationDto[];

            this.loading = false;
          }).catch(err => {
            this.error = 'Erreur lors du traitement des réservations.';
            this.loading = false;
            console.error(err);
          });
        } catch (e) {
          this.error = 'Erreur inattendue.';
          this.loading = false;
          console.error(e);
        }
      },
      error: (err: any) => {
        this.error = 'Impossible de charger les réservations.';
        this.loading = false;
        console.error(err);
      }
    });
  }

  toggleShowAll(): void {
    this.showAll = !this.showAll;
  }

  loadRevenue(): void {
    this.revenueLoading = true;
    this.revenueError = null;
    this.revenue = null;

    const technicianId = this.authService.getUserId();
    if (!technicianId) {
      console.warn('Current user has no id (getUserId returned null)');
      this.revenueError = 'Utilisateur introuvable (identifiant technicien manquant)';
      this.revenueLoading = false;
      return;
    }

    this.reservationsStatsService.getTechnicianRevenue(technicianId, 'response').subscribe({
      next: (resp: any) => {
        const body = resp?.body ?? resp;
        const parseJson = (data: any) => {
          if (data instanceof Blob) {
            return new Promise<any>((resolve, reject) => {
              const reader = new FileReader();
              reader.onload = () => {
                try {
                  resolve(JSON.parse(reader.result as string));
                } catch (err) {
                  reject(err);
                }
              };
              reader.onerror = (e) => reject(e);
              reader.readAsText(data as Blob);
            });
          }
          return Promise.resolve(data);
        };

        Promise.resolve(parseJson(body)).then((val: any) => {
          // backend returns a BigDecimal, may come as string like "245000.00"
          const num = val == null ? 0 : (typeof val === 'number' ? val : parseFloat(String(val)));
          this.revenue = isFinite(num) ? num : 0;
          this.revenueLoading = false;
        }).catch(err => {
          this.revenueError = 'Erreur lors du parsing du revenu';
          this.revenueLoading = false;
          console.error(err);
        });
      },
      error: (err: any) => {
        this.revenueError = 'Impossible de charger le revenu';
        this.revenueLoading = false;
        console.error(err);
      }
    });

  }

  loadRating(): void {
    this.ratingLoading = true;
    this.ratingError = null;
    this.rating = null;

    const technicianId = this.authService.getUserId();
    if (!technicianId) {
      console.warn('Current user has no id (getUserId returned null)');
      this.ratingError = 'Utilisateur introuvable (identifiant technicien manquant)';
      this.ratingLoading = false;
      return;
    }

    this.reservationsStatsService.getTechnicianAverageRating(technicianId, 'response').subscribe({
      next: (resp: any) => {
        const body = resp?.body ?? resp;
        const parseJson = (data: any) => {
          if (data instanceof Blob) {
            return new Promise<any>((resolve, reject) => {
              const reader = new FileReader();
              reader.onload = () => {
                try {
                  resolve(JSON.parse(reader.result as string));
                } catch (err) {
                  reject(err);
                }
              };
              reader.onerror = (e) => reject(e);
              reader.readAsText(data as Blob);
            });
          }
          return Promise.resolve(data);
        };

        Promise.resolve(parseJson(body)).then((val: any) => {
          const num = val == null ? 0 : (typeof val === 'number' ? val : parseFloat(String(val)));
          this.rating = isFinite(num) ? num : 0;
          this.ratingLoading = false;
        }).catch(err => {
          this.ratingError = 'Erreur lors du parsing de la note';
          this.ratingLoading = false;
          console.error(err);
        });
      },
      error: (err: any) => {
        this.ratingError = 'Impossible de charger la note moyenne';
        this.ratingLoading = false;
        console.error(err);
      }
    });

  }

  /**
   * Return an array of star types for display: 'full' | 'half' | 'empty'
   */
  getStarsArray(): ('full' | 'half' | 'empty')[] {
    const r = this.rating ?? 0;
    const stars: ('full' | 'half' | 'empty')[] = [];
    for (let i = 1; i <= 5; i++) {
      const value = r - (i - 1);
      if (value >= 1) {
        stars.push('full');
      } else if (value >= 0.5) {
        stars.push('half');
      } else {
        stars.push('empty');
      }
    }
    return stars;
  }

}
