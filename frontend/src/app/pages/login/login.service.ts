import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';

interface AuthResponse {
  token: string;
  email?: string;
  role: string;
}

@Injectable({
  providedIn: 'root',
})
export class LoginService {
  private apiUrl = 'http://localhost:8080/api';
  private http = inject(HttpClient);

  login(user: { email: string; password: string }): Observable<AuthResponse> {
    console.log('Attempting to login with user:', user);
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post<AuthResponse>(`${this.apiUrl}/auth/login`, user, { headers }).pipe(
      tap(response => {
        localStorage.setItem('token', response.token);
        localStorage.setItem('role', response.role);
        console.log('Login successful, token stored:', response.token, 'role:', response.role);
      }),
      catchError((error: HttpErrorResponse) => {
        console.error('Login failed', error);
        console.error('Raw error response:', error.error);
        return throwError(() => error);
      })
    );
  }

  logout(): Observable<{ message: string }> { // Match the backend response
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });
    return this.http.post<{ message: string }>(`${this.apiUrl}/users/logout`, {}, { headers }).pipe(
      tap(() => {
        localStorage.removeItem('token');
        localStorage.removeItem('role');
        console.log('Logout successful, token and role removed from localStorage');
      }),
      catchError((error: HttpErrorResponse) => {
        console.error('Logout failed', error);
        return throwError(() => error);
      })
    );
  }

  getUserRole(): string | null {
    const role = localStorage.getItem('role');
    console.log('Retrieved role from localStorage:', role);
    return role;
  }
}