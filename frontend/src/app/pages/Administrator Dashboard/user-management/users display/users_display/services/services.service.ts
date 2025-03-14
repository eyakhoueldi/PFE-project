import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class Services {
    private apiUrl = 'http://localhost:8080/api';

    constructor(private http: HttpClient) {}

    get<T>(endpoint: string): Observable<T> {
        const token = localStorage.getItem('token');
        const headers = token ? new HttpHeaders().set('Authorization', `Bearer ${token}`) : new HttpHeaders();
        return this.http.get<T>(`${this.apiUrl}/${endpoint}`, { headers });
    }

    post<T>(endpoint: string, body: any): Observable<T> {
        const token = localStorage.getItem('token');
        const headers = token ? new HttpHeaders().set('Authorization', `Bearer ${token}`) : new HttpHeaders();
        return this.http.post<T>(`${this.apiUrl}/${endpoint}`, body, { headers });
    }

    logout(): Observable<any> { // Ensure this method exists
        const token = localStorage.getItem('token');
        const headers = token ? new HttpHeaders().set('Authorization', `Bearer ${token}`) : new HttpHeaders();
        return this.http.post(`${this.apiUrl}/users/logout`, {}, { headers });
    }
}