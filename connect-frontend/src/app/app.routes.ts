import { Routes } from '@angular/router';
import { HomePageComponent } from './features/home/pages/home-page/home-page.component';
import { RegisterComponent } from './features/auth/register/register.component';
import { LoginComponent } from './features/auth/login/login.component';
import { ExplorationComponent } from './features/explorer/page/exploration/exploration.component';
import { authGuard } from './guards/auth.guard';
import { adminGuard } from './guards/admin.guard';
import { FormTechComponent } from './features/technician/pages/form-tech/form-tech.component';
import { DashboardTechnicienComponent } from './features/technician/pages/dashboard-technicien/dashboard-technicien.component';
import { TechSkillsComponent } from './features/technician/pages/tech-skills/tech-skills.component';
import { TodayReservationsComponent } from './features/technician/pages/today-reservations/today-reservations.component';
import { ReservationDetailComponent } from './features/technician/pages/reservation-detail/reservation-detail.component';
import { PendingReservationsComponent } from './features/technician/pages/pending-reservations/pending-reservations.component';
import { TechnicianReservationsComponent } from './features/technician/pages/technician-reservations/technician-reservations.component';
import { TechnicianProfileComponent } from './features/technician/pages/technician-profile/technician-profile.component';

export const routes: Routes = [
  { path: '', component: HomePageComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'login', component: LoginComponent },
  { path: "devenir-technicien", component: FormTechComponent },
  { path: 'catalogue', component: ExplorationComponent, canActivate: [authGuard] },
  { path: 'dashboard-technicien', component: DashboardTechnicienComponent, canActivate: [authGuard] },
  { path: 'dashboard-technicien/skill', component: TechSkillsComponent, canActivate: [authGuard] },
  { path: 'dashboard-technicien/today', component: TodayReservationsComponent, canActivate: [authGuard] },
  { path: 'dashboard-technicien/reservations', component: TechnicianReservationsComponent, canActivate: [authGuard] },
  { path: 'dashboard-technicien/reservation/:id', component: ReservationDetailComponent, canActivate: [authGuard] },
  { path: 'dashboard-technicien/pending', component: PendingReservationsComponent, canActivate: [authGuard] },
  { path: 'dashboard-technicien/profil', component: TechnicianProfileComponent, canActivate: [authGuard] },
  {
    path: 'dashboard-admin',
    loadChildren: () => import('./features/admin/admin.routes').then(m => m.ADMIN_ROUTES),
    canActivate: [adminGuard]
  },
  {
    path: 'client',
    loadChildren: () => import('./features/client/client.routes').then(m => m.CLIENT_ROUTES),
    canActivate: [authGuard]
  },
  { path: '**', redirectTo: 'login' } // Wildcard route for a 404 page
];
