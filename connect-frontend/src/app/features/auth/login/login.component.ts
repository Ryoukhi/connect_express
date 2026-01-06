import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterLink, RouterModule } from '@angular/router';
import { AuthService } from '../../../services/auth.service';
import { AuthRequest } from '../../../api/models';

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
      email: this.email!.value,      // safe access avec nonNullable
      password: this.password!.value
    };

    this.authService.login(authRequest).subscribe({
      next: (res) => {
        if (!res?.token) {
          console.error('Login succeeded but no token in response', res);
          return;
        }
        this.authService.storeSession(res);
        setTimeout(() => this.router.navigate(['/catalogue']), 200);
      },
      error: (err) => {
        this.submitting = false;
        this.serverError = err?.error?.message || 'Email ou mot de passe incorrect';
      }
    });
  }
}
