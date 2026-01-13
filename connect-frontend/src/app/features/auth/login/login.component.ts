import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterLink, RouterModule } from '@angular/router';
import { AuthService } from '../../../services/auth.service';
import { AuthRequest } from '../../../api/models';
import { TechniciensService } from '../../../api/services/techniciens.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink, RouterModule],
  templateUrl: './login.component.html'
})
export class LoginComponent {

  loginForm: FormGroup;
  submitting = false;
  serverError: string | null = null;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private techniciensService: TechniciensService,
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

        const userId = this.authService.getUserId();

        if (!userId) {
          console.warn('UserId introuvable, redirection catalogue');
          this.router.navigate(['/catalogue']);
          this.submitting = false;
          return;
        }

        // 🔍 Vérifie si le user est technicien
        this.techniciensService.getTechnicianById(userId, 'body').subscribe({
          next: () => {
            this.router.navigate(['/dashboard-technicien']);
            this.submitting = false;
          },
          error: () => {
            this.router.navigate(['/catalogue']);
            this.submitting = false;
          }
        });
      },
      error: (err) => {
        this.submitting = false;
        this.serverError = err?.error?.message || 'Email ou mot de passe incorrect';
      }
    });
  }
}
