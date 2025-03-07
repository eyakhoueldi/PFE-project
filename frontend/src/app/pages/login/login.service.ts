import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

interface AuthResponse {
  token: string;
}

@Injectable({
  providedIn: 'root',
})
export class LoginService {
  private apiUrl = 'http://localhost:8080/api/auth/login';
  private http = inject(HttpClient);

  login(user: { email: string; password: string }): Observable<AuthResponse> {
    console.log("Attempting to login with user:", user);  // Debug log for user data

    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });

    return this.http.post<AuthResponse>(this.apiUrl, user, { headers }).pipe(
      catchError(error => {
        console.error('Login failed', error);  // Debug log for error
        console.error('Error details:', error.error); // Log the error response body
        return throwError(() => error); // Return the full error object
      })
    );
  }
}