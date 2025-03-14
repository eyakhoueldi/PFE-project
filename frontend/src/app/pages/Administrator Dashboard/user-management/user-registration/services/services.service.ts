// src/app/services/user.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { UserRole } from '../../../../../user-role.enum';
@Injectable({
  providedIn: 'root',
})
export class UserService { // Renamed from Services to UserService
  private apiUrl = 'http://localhost:8080/api/users'; // Full backend URL

  constructor(private http: HttpClient) {}

  // Method to create a user
  createUser(email: string, password: string, role: UserRole): Observable<any> {
    const userData = { email, password, role };
    return this.http.post(`${this.apiUrl}/create`, userData);
  }
}