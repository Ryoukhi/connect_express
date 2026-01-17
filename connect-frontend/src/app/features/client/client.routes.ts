import { Routes } from '@angular/router';
import { BookingFormComponent } from './pages/booking-form/booking-form.component';
import { ClientReservationsComponent } from './pages/client-reservations/client-reservations.component';
import { ReservationDetailComponent } from './pages/reservation-detail/reservation-detail.component';
import { ClientProfileComponent } from './pages/client-profile/client-profile.component';

export const CLIENT_ROUTES: Routes = [
    { path: 'book/:technicianId', component: BookingFormComponent },
    { path: 'reservations', component: ClientReservationsComponent },
    { path: 'reservations/:id', component: ReservationDetailComponent },
    { path: 'profil', component: ClientProfileComponent }
];
