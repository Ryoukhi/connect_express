import { inject, Injectable } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (authService.isLoggedIn()) {
    return true; // L'utilisateur est connecté, autorisé
  } else {
    // Redirige vers login avec returnUrl
    router.navigate(['/login'], { queryParams: { returnUrl: state.url } });
    return false;
  }
};
