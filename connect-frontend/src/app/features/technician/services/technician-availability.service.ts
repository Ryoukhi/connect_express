import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { TechniciensService } from '../../../api/services/techniciens.service';

@Injectable({ providedIn: 'root' })
export class TechnicianAvailabilityService {

  constructor(private techniciensApi: TechniciensService) { }

  /**
   * Met à jour le statut de disponibilité du technicien
   */
  updateAvailabilityStatus(technicianId: number, status: 'AVAILABLE' | 'BUSY' | 'UNAVAILABLE' | 'ON_BREAK'): Observable<any> {
    return this.techniciensApi.updateAvailabilityStatus(technicianId, { availabilityStatus: status }, 'body');
  }

  /**
   * Récupère le profil complet du technicien
   */
  getTechnicianProfile(technicianId: number): Observable<any> {
    return this.techniciensApi.getTechnicianById(technicianId, 'body');
  }

}
