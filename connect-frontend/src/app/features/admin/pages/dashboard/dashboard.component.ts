import { Component } from '@angular/core';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent {
  stats = [
    { title: 'Utilisateurs', value: '1,234', icon: '👥', color: 'bg-blue-100 text-blue-600' },
    { title: 'Techniciens', value: '56', icon: '👷', color: 'bg-green-100 text-green-600' },
    { title: 'Réservations', value: '89', icon: '📅', color: 'bg-purple-100 text-purple-600' },
    { title: 'Revenus', value: '12,340 FCFA', icon: '💰', color: 'bg-yellow-100 text-yellow-600' }
  ];
}
