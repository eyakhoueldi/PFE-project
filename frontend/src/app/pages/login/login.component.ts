import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { LoginService } from './login.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  standalone: true,
  imports: [FormsModule, CommonModule],
})
export class LoginComponent {
  email: string = '';
  password: string = '';
  message: string = '';
  isLoggedIn: boolean = !!localStorage.getItem('token');

  private router = inject(Router);
  private loginService = inject(LoginService);

  onSubmit() {
    if (!this.email || !this.password) {
      this.message = 'Please enter your email and password.';
      return;
    }

    console.log('Login form submitted');

    const user = { email: this.email, password: this.password };

    this.loginService.login(user).subscribe({
      next: (response: any) => {
        if ('token' in response && 'role' in response) {
          const token = (response as { token: string }).token;
          const role = (response as { role: string }).role;
          localStorage.setItem('token', token);
          localStorage.setItem('role', role);
          console.log('Login successful, token:', token, 'role:', role);

          this.isLoggedIn = true;

          switch (role) {
            case 'ADMINISTRATOR':
              this.router.navigate(['/admin/dashboard']).then(success => {
                if (!success) console.error('Navigation to admin dashboard failed');
              });
              break;
            case 'DEV_TEAM_MEMBER':
              this.router.navigate(['/dev']).then(success => {
                if (!success) console.error('Navigation to dev failed');
              });
              break;
            case 'PROJECT_MANAGER':
              this.router.navigate(['/pm']).then(success => {
                if (!success) console.error('Navigation to pm failed');
              });
              break;
            default:
              this.router.navigate(['/dashboard']).then(success => {
                if (!success) console.error('Navigation to dashboard failed');
              });
              break;
          }
          this.message = 'Login successful!';
        } else {
          this.message = 'Unexpected response from server.';
        }
      },
      error: (error: any) => {
        console.error('Login Error:', error);
        this.message = error.error?.error || 'Login failed due to an unexpected error.';
      },
    });
  }

  logout() {
    this.loginService.logout().subscribe({
      next: (response: { message: string }) => { // Type the response
        this.isLoggedIn = false;
        this.message = response.message || 'Logged out successfully';
        this.email = '';
        this.password = '';
        this.router.navigate(['/login']);
      },
      error: (error: { error: { error: string } }) => { // Type the error
        console.error('Logout Error:', error);
        this.message = error.error?.error || 'Logout failed';
      }
    });
  }
}