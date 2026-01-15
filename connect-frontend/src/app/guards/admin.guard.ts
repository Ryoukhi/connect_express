import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

export const adminGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (authService.isLoggedIn()) {
    // TODO: Add role check here when backend supports roles in token/user object
    // const user = authService.getCurrentUser();
    // if (user?.role === 'ADMIN') return true;
    return true;
  } else {
    router.navigate(['/login'], { queryParams: { returnUrl: state.url } });
    return false;
  }
};
