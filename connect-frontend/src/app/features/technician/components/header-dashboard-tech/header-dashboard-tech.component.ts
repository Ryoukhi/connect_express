import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../../../services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-header-dashboard-tech',
  imports: [CommonModule],
  templateUrl: './header-dashboard-tech.component.html',
  styleUrl: './header-dashboard-tech.component.css'
})
export class HeaderDashboardTechComponent implements OnInit {

  fullName = '';
  showMenu = false;

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.fullName = this.authService.getFullName();
  }

  toggleMenu(): void {
    this.showMenu = !this.showMenu;
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

}
