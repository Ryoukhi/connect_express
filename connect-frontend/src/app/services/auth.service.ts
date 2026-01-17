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

  getUserRole(): string | null {
    const user = this.getUser();
    return user?.role ?? null;
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

    const rawUser = respBody?.user ?? respBody?.data?.user ?? respBody ?? response;
    // Normalize user object to provide a stable `userId` property for consumers
    const normalizedUser: any = { ...(rawUser || {}) };
    const idCand = rawUser?.userId ?? rawUser?.id ?? (rawUser as any)?.idUser ?? (rawUser as any)?.id_user ?? (rawUser as any)?.user_id ?? null;
    if (idCand != null) {
      normalizedUser.userId = typeof idCand === 'string' ? Number(idCand) : idCand;
    }
    try {
      localStorage.setItem(this.USER_KEY, JSON.stringify(normalizedUser));
      console.log('storeSession: user saved (normalized)', normalizedUser);
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
  getCurrentUser(): AuthResponse | null {
    const userStr = localStorage.getItem(this.USER_KEY);
    if (!userStr) return null;

    try {
      const user = JSON.parse(userStr) as any;
      // Ensure userId exists and persist normalized object if needed
      const idCand = user?.userId ?? user?.id ?? user?.idUser ?? user?.id_user ?? user?.user_id ?? null;
      if ((user as any).userId == null && idCand != null) {
        (user as any).userId = typeof idCand === 'string' ? Number(idCand) : idCand;
        try {
          localStorage.setItem(this.USER_KEY, JSON.stringify(user));
          console.log('getCurrentUser: normalized stored user with userId', (user as any).userId);
        } catch (err) {
          console.warn('getCurrentUser: failed to persist normalized user', err);
        }
      }

      return user as AuthResponse;
    } catch (err) {
      console.warn('getCurrentUser: failed to parse stored user', err);
      return null;
    }
  }

  /**
   * Returns the detected numeric user id for the current session.
   * Tries stored `auth_user` then decodes JWT payload to find common id claims.
   */
  getUserId(): number | null {
    try {
      const user = this.getCurrentUser() as any;
      const idCand = user?.userId ?? user?.id ?? user?.idUser ?? user?.id_user ?? user?.user_id ?? null;
      if (idCand != null) return typeof idCand === 'string' ? Number(idCand) : idCand;

      const token = this.getToken();
      if (!token) return null;

      const base64 = token.split('.')[1];
      if (!base64) return null;

      // base64url -> base64
      const corrected = base64.replace(/-/g, '+').replace(/_/g, '/');
      // decode percent-encoding properly
      const b64DecodeUnicode = (str: string) => decodeURIComponent(Array.prototype.map.call(atob(str), (c: string) => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2)).join(''));
      const payload = JSON.parse(b64DecodeUnicode(corrected));

      const tId = payload?.userId ?? payload?.id ?? payload?.sub ?? payload?.user_id ?? payload?.idUser ?? null;
      if (tId != null) return typeof tId === 'string' ? Number(tId) : tId;
    } catch (err) {
      console.warn('getUserId: failed to detect user id from storage or token', err);
    }

    return null;
  }

  getFullName(): string {
    const user = this.getCurrentUser();
    if (!user) return '';

    return `${user.firstName ?? ''} ${user.lastName ?? ''}`.trim();
  }


}


