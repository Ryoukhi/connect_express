import { Component } from '@angular/core';
import { AuthService } from '../../../services/auth.service';
import { CommonModule } from '@angular/common';
import { Router, RouterLink, RouterModule } from '@angular/router';

@Component({
  selector: 'app-client-header',
  imports: [RouterModule, RouterLink, CommonModule],
  templateUrl: './client-header.component.html',
  styleUrl: './client-header.component.css'
})
export class ClientHeaderComponent {

  fullName = '';
  profilePhotoUrl: string | null = null;
  showMenu = false;

  constructor(private authService: AuthService, private router: Router) { }

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
