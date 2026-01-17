import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../../services/auth.service';
import { AuthRequest } from '../../../api/models';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './login.component.html'
})
export class LoginComponent {

  loginForm: FormGroup;
  submitting = false;
  serverError: string | null = null;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    // Formulaire avec nonNullable pour éviter null
    this.loginForm = this.fb.nonNullable.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required]
    });
  }

  get email() { return this.loginForm.get('email'); }
  get password() { return this.loginForm.get('password'); }

  onSubmit(): void {
    this.serverError = null;

    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();
      return;
    }

    this.submitting = true;

    const authRequest: AuthRequest = {
      email: this.email!.value,
      password: this.password!.value
    };

    this.authService.login(authRequest).subscribe({
      next: (res) => {
        if (!res?.token) {
          console.error('Login réussi mais token manquant', res);
          this.serverError = 'Erreur interne d’authentification';
          this.submitting = false;
          return;
        }

        // 🔐 Sauvegarde session
        this.authService.storeSession(res);

        const userRole = this.authService.getUserRole();

        if (!userRole) {
          console.warn('Rôle utilisateur introuvable');
          this.router.navigate(['/catalogue']);
          this.submitting = false;
          return;
        }

        // 🔍 Redirige en fonction du rôle
        if (userRole === 'TECHNICIAN') {
          this.router.navigate(['/dashboard-technicien']);
        }
        else if (userRole === 'ADMIN') {
          this.router.navigate(['/dashboard-admin']);
        } else {
          this.router.navigate(['/catalogue']);
        }
        this.submitting = false;
      },
      error: (err) => {
        this.submitting = false;
        this.serverError = err?.error?.message || 'Email ou mot de passe incorrect';
      }
    });
  }
}
