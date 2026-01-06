import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, Validators, ReactiveFormsModule, FormGroup } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../services/auth.service';
import { AuthRequest } from '../../../api/models';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './login.component.html'
})
export class LoginComponent {

  loginForm!: FormGroup;
  submitting = false;
  serverError: string | null = null;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.loginForm = this.fb.nonNullable.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required]
    });
  }

 onSubmit() {
  this.serverError = null;

  if (this.loginForm.invalid) {
    this.loginForm.markAllAsTouched();
    return;
  }

  this.submitting = true;

  const formValue = this.loginForm.value;
  const authRequest: AuthRequest = {
    email: formValue['email'],
    password: formValue['password'],
  };

  this.authService.login(authRequest).subscribe({
    next: () => {
      this.submitting = false;
      this.router.navigate(['/dashboard']);
    },
    error: (err) => {
      this.submitting = false;
      this.serverError =
        err?.error?.message || 'Email ou mot de passe incorrect';
    }
  });
}
}
