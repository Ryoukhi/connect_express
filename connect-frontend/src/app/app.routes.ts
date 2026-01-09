import { Routes } from '@angular/router';
import { HomePageComponent } from './features/home/pages/home-page/home-page.component';
import { RegisterComponent } from './features/auth/register/register.component';
import { LoginComponent } from './features/auth/login/login.component';
import { ExplorationComponent } from './features/explorer/page/exploration/exploration.component';
import { authGuard } from './guards/auth.guard';
import { FormTechComponent } from './features/technician/pages/form-tech/form-tech.component';
import { DashboardTechnicienComponent } from './features/technician/pages/dashboard-technicien/dashboard-technicien.component';

export const routes: Routes = [
  {path: '', component: HomePageComponent},
  {path: 'register', component: RegisterComponent},
  {path: 'login', component: LoginComponent},
  {path: "devenir-technicien", component: FormTechComponent},
  {path: 'catalogue', component: ExplorationComponent, canActivate: [authGuard]},
  {path: 'dashboard-technicien', component: DashboardTechnicienComponent, canActivate: [authGuard]},
  {path: '**', redirectTo: 'login'} // Wildcard route for a 404 page
];
