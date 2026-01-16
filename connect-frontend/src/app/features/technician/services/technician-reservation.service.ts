import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ReservationsService } from '../../../api/services/reservations.service';
import { ReservationDto } from '../../../api/models';

@Injectable({ providedIn: 'root' })
export class TechnicianReservationService {

  constructor(private reservationApi: ReservationsService) { }

  /**
   * Récupère toutes les réservations du technicien connecté
   */
  getMyReservations(): Observable<ReservationDto[]> {
    return this.reservationApi.getMyTechnicianReservations('body');
  }

  /**
   * Récupère une réservation par ID
   */
  getReservationById(id: number): Observable<ReservationDto> {
    return this.reservationApi.getById(id, 'body');
  }

  /**
   * Change le statut d'une réservation
   */
  changeStatus(id: number, status: 'PENDING' | 'ACCEPTED' | 'EN_ROUTE' | 'IN_PROGRESS' | 'COMPLETED' | 'CANCELLED' | 'REJECTED'): Observable<ReservationDto> {
    return this.reservationApi.changeStatus(id, status, 'body');
  }

  /**
   * Accepte une réservation (change le statut à ACCEPTED)
   */
  acceptReservation(id: number): Observable<ReservationDto> {
    return this.changeStatus(id, 'ACCEPTED');
  }

  /**
   * Refuse une réservation (change le statut à REJECTED)
   */
  rejectReservation(id: number, reason?: string): Observable<ReservationDto> {
    return this.reservationApi.cancelReservation(id, reason, 'body');
  }

  /**
   * Marque une réservation comme complétée
   */
  completeReservation(id: number): Observable<ReservationDto> {
    return this.reservationApi.completeReservation(id, 'body');
  }

  /**
   * Met à jour le statut à EN_ROUTE
   */
  startRoute(id: number): Observable<ReservationDto> {
    return this.changeStatus(id, 'EN_ROUTE');
  }

  /**
   * Met à jour le statut à IN_PROGRESS
   */
  startWork(id: number): Observable<ReservationDto> {
    return this.changeStatus(id, 'IN_PROGRESS');
  }

  /**
   * Annule une réservation
   */
  cancelReservation(id: number, reason: string): Observable<ReservationDto> {
    return this.reservationApi.cancelReservation(id, reason, 'body');
  }

}
