import { Injectable } from '@angular/core';
import { Observable, tap } from 'rxjs';
import { AuthControllerService } from '../api/services/authController.service';
import { AuthRequest, AuthResponse } from '../api/models';
// import jwtDecode from 'jwt-decode';

@Injectable({ providedIn: 'root' })
export class AuthService {

  private readonly TOKEN_KEY = 'access_token';
  private readonly USER_KEY = 'auth_user';

  constructor(private authApi: AuthControllerService) {}

  login(request: AuthRequest): Observable<AuthResponse> {
    return this.authApi.login(request, 'body').pipe(
      tap((response) => {
        this.storeSession(response);
      })
    );
  }

  logout(idUser: number): Observable<void> {
    return this.authApi.logout(idUser, 'body').pipe(
      tap(() => this.clearSession())
    );
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

  private storeSession(response: AuthResponse): void {
    if (!response.token) {
      throw new Error('JWT token manquant dans la réponse d’authentification');
    }

    localStorage.setItem(this.TOKEN_KEY, response.token);
    localStorage.setItem(this.USER_KEY, JSON.stringify(response));
  }

  private clearSession() {
    localStorage.removeItem(this.TOKEN_KEY);
    localStorage.removeItem(this.USER_KEY);
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


