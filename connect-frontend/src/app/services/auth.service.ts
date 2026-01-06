import { Injectable } from '@angular/core';
import { Observable, tap } from 'rxjs';
import { AuthControllerService } from '../api/services/authController.service';
import { AuthRequest, AuthResponse, RegisterResponseDto } from '../api/models';
import { Router } from '@angular/router';
// import jwtDecode from 'jwt-decode';

@Injectable({ providedIn: 'root' })
export class AuthService {

  private readonly TOKEN_KEY = 'access_token';
  private readonly USER_KEY = 'auth_user';

  constructor(private authApi: AuthControllerService, private router: Router) {}

  login(request: AuthRequest): Observable<AuthResponse> {
    return this.authApi.login(request, 'body').pipe(
      tap((response) => {
        this.storeSession(response);
      })
    );
  }

  logout(): void {
    localStorage.removeItem(this.TOKEN_KEY);
    localStorage.removeItem(this.USER_KEY);
    this.router.navigate(['/login']);
  }

  isAuthenticated(): boolean {
    return !!this.getToken();
  }

  getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  getUser(): AuthResponse | null {
    const user = localStorage.getItem(this.USER_KEY);
    return user ? JSON.parse(user) : null;
  }

  storeSession(response: RegisterResponseDto | AuthResponse | any): boolean {

    // Some HTTP clients return a full HttpResponse object; handle both shapes (body or direct object)
    const respBody = response?.body ?? response;

    const token = respBody?.token ?? respBody?.accessToken ?? respBody?.access_token ?? respBody?.data?.token ?? null;
    if (!token) {
      console.error('Aucun token reçu lors de storeSession — réponse complète :', response);
      return false;
    }

    try {
      localStorage.setItem(this.TOKEN_KEY, token);
      const verify = localStorage.getItem(this.TOKEN_KEY);
      console.log('storeSession: token saved', { token, verify, response: respBody });
    } catch (err) {
      console.error('storeSession: failed to save token to localStorage', err);
      return false;
    }

    const user = respBody?.user ?? respBody?.data?.user ?? respBody ?? response;
    try {
      localStorage.setItem(this.USER_KEY, JSON.stringify(user));
      console.log('storeSession: user saved', user);
    } catch (err) {
      console.error('storeSession: failed to save user to localStorage', err);
    }

    return true;
  }

  isLoggedIn(): boolean {
    const token = localStorage.getItem(this.TOKEN_KEY);
    // Tu peux ajouter une vérification de validité du token si tu veux
    return !!token;
  }

  // Vérifie si le token est expiré
  // isTokenExpired(token: string): boolean {
  //   try {
  //     const decoded: any = jwtDecode(token);
  //     const exp = decoded.exp;
  //     if (!exp) return true;

  //     const now = Math.floor(Date.now() / 1000);
  //     return exp < now;
  //   } catch (error) {
  //     return true;
  //   }
  // }

}


