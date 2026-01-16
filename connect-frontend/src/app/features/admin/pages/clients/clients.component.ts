import { Component, inject, OnInit } from '@angular/core';
import { AdminService, UserDto } from '../../services/admin.service';
import { CommonModule, DatePipe } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-clients',
  standalone: true,
  imports: [CommonModule, FormsModule, DatePipe],
  templateUrl: './clients.component.html',
  styleUrl: './clients.component.css'
})
export class ClientsComponent implements OnInit {
  private adminService = inject(AdminService);

  clients: UserDto[] = [];

  ngOnInit() {
    this.loadClients();
  }

  loadClients() {
    this.adminService.getClients().subscribe(res => {
      this.clients = res;
    });
  }

  deleteClient(id: number) {
    if (confirm('Supprimer définitivement ce client ?')) {
      this.adminService.deleteUser(id).subscribe(() => this.loadClients());
    }
  }
}
