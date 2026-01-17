import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../../../services/auth.service';
import { Router, RouterModule } from '@angular/router';

@Component({
  selector: 'app-header-dashboard-tech',
  imports: [CommonModule, RouterModule],
  templateUrl: './header-dashboard-tech.component.html',
  styleUrl: './header-dashboard-tech.component.css'
})
export class HeaderDashboardTechComponent implements OnInit {

  fullName = '';
  showMenu = false;
  profilePhotoUrl: string | null = null;

  constructor(
    private authService: AuthService,
    private router: Router,
  ) { }

  ngOnInit(): void {
    const user = this.authService.getUser();
    this.fullName = this.authService.getFullName();
    this.profilePhotoUrl = user?.profilePhotoUrl || null;
  }

  toggleMenu(): void {
    this.showMenu = !this.showMenu;
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  get avatarUrl(): string {
    return this.profilePhotoUrl?.trim()
      ? this.profilePhotoUrl
      : `https://ui-avatars.com/api/?name=${encodeURIComponent(this.fullName)}&background=2563eb&color=fff`;
  }

}
