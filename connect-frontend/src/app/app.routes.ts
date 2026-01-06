import { Routes } from '@angular/router';
import { HomePageComponent } from './features/home/pages/home-page/home-page.component';
import { RegisterComponent } from './features/auth/register/register.component';
import { LoginComponent } from './features/auth/login/login.component';
import { ExplorationComponent } from './features/explorer/page/exploration/exploration.component';
import { authGuard } from './guards/auth.guard';

export const routes: Routes = [
  {path: '', component: HomePageComponent},
  {path: 'register', component: RegisterComponent},
  {path: 'login', component: LoginComponent},
  {path: 'catalogue', component: ExplorationComponent, canActivate: [authGuard]},
  {path: '**', redirectTo: 'login'} // Wildcard route for a 404 page
];
