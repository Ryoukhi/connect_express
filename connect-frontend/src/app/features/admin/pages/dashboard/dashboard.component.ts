import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AdminService } from '../../services/admin.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent implements OnInit {
  private adminService = inject(AdminService);

  stats = [
    { title: 'Utilisateurs', value: '...', icon: '👥', color: 'bg-blue-100 text-blue-600' },
    { title: 'Techniciens', value: '...', icon: '👷', color: 'bg-green-100 text-green-600' },
    { title: 'Réservations', value: '...', icon: '📅', color: 'bg-purple-100 text-purple-600' },
    { title: 'Revenus', value: '...', icon: '💰', color: 'bg-yellow-100 text-yellow-600' }
  ];

  ngOnInit() {
    this.loadStats();
  }

  loadStats() {
    this.adminService.getGeneralStatistics().subscribe({
      next: (data) => {
        this.stats = [
          { title: 'Clients', value: data.activeClients?.toString() || '0', icon: '👥', color: 'bg-blue-100 text-blue-600' },
          { title: 'Techniciens Actifs', value: data.activeTechnicians?.toString() || '0', icon: '👷', color: 'bg-green-100 text-green-600' },
          { title: 'Réservations', value: data.totalReservations?.toString() || '0', icon: '📅', color: 'bg-purple-100 text-purple-600' },
          { title: 'Revenus', value: (data.totalRevenue?.toString() || '0') + ' FCFA', icon: '💰', color: 'bg-yellow-100 text-yellow-600' }
        ];
      },
      error: (err) => console.error('Failed to load stats', err)
    });
  }
}
