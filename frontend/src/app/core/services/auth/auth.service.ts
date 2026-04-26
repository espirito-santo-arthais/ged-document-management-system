import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { LoginRequest, LoginResponse } from '../../models/auth.model';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private readonly apiUrl = `${environment.apiUrl}/auth`;

  isAuthenticated = signal<boolean>(this.hasToken());

  constructor(private http: HttpClient) { }

  /**
   * Realiza o login e salva o token JWT.
   */
  login(data: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.apiUrl}/login`, data).pipe(
      tap((res) => {
        // Embora o backend envie o "type", o interceptor geralmente 
        // já fixa o "Bearer". Salvamos apenas o token puro.
        this.saveToken(res.token);
      })
    );
  }

  saveToken(token: string): void {
    localStorage.setItem('token', token);
    this.isAuthenticated.set(true);
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  logout(): void {
    localStorage.removeItem('token');
    this.isAuthenticated.set(false);
  }

  private hasToken(): boolean {
    return !!this.getToken();
  }
}