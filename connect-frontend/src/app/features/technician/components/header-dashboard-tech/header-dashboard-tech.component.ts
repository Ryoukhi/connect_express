import { Component } from '@angular/core';
import { AuthService } from '../../../../services/auth.service';

@Component({
  selector: 'app-header-dashboard-tech',
  imports: [],
  templateUrl: './header-dashboard-tech.component.html',
  styleUrl: './header-dashboard-tech.component.css'
})
export class HeaderDashboardTechComponent {

  fullName = '';

  constructor(private authService: AuthService) {}

  ngOnInit(): void {
    this.fullName = this.authService.getFullName();
  }
}
