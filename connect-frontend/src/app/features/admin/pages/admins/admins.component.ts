import { Component, inject, OnInit } from '@angular/core';
import { AdminService, AdminUser } from '../../services/admin.service';
import { CommonModule, DatePipe } from '@angular/common';

@Component({
  selector: 'app-admins',
  standalone: true,
  imports: [CommonModule, DatePipe],
  templateUrl: './admins.component.html',
  styleUrl: './admins.component.css'
})
export class AdminsComponent implements OnInit {
  private adminService = inject(AdminService);

  admins: AdminUser[] = [];

  ngOnInit() {
    this.loadAdmins();
  }

  loadAdmins() {
    this.adminService.getAdmins().subscribe(res => {
      this.admins = res;
    });
  }

  deleteAdmin(id: number) {
    if (confirm('Supprimer cet administrateur ?')) {
      this.adminService.deleteAdmin(id).subscribe(() => this.loadAdmins());
    }
  }

  getRoleLabel(role: string): string {
    return role === 'SUPER_ADMIN' ? 'Super Admin' : 'Admin';
  }
}
