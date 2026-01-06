import { Component } from '@angular/core';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-client-header',
  imports: [],
  templateUrl: './client-header.component.html',
  styleUrl: './client-header.component.css'
})
export class ClientHeaderComponent {

   fullName = '';

  constructor(private authService: AuthService) {}

  ngOnInit(): void {
    this.fullName = this.authService.getFullName();
  }

}
