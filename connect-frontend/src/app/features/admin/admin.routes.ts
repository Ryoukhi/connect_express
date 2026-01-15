import { Routes } from '@angular/router';
import { AdminLayoutComponent } from './layout/admin-layout/admin-layout.component';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { CategoriesComponent } from './pages/categories/categories.component';
import { TechniciansComponent } from './pages/technicians/technicians.component';
import { ClientsComponent } from './pages/clients/clients.component';
import { AdminsComponent } from './pages/admins/admins.component';

export const ADMIN_ROUTES: Routes = [
  {
    path: '',
    component: AdminLayoutComponent,
    children: [
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
      { path: 'dashboard', component: DashboardComponent },
      { path: 'categories', component: CategoriesComponent },
      { path: 'technicians', component: TechniciansComponent },
      { path: 'clients', component: ClientsComponent },
      { path: 'admins', component: AdminsComponent },
    ]
  }
];
