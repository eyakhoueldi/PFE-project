import { Component, EventEmitter, Output, inject } from '@angular/core';
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
  @Output() onSubmitLoginEvent = new EventEmitter();

  email: string = '';
  password: string = '';
  message: string = '';

  private router = inject(Router);
  private loginService = inject(LoginService);

  onSubmit() {
    if (!this.email || !this.password) {
      this.message = 'Please enter your email and password.';
      return;
    }

    console.log("Login form submitted");  // Debug log for submission

    const user = { email: this.email, password: this.password };
    this.onSubmitLoginEvent.emit(user);

    this.loginService.login(user).subscribe({
      next: (response) => {
        console.log('Login response:', response); // Debug log for response
        const token = response.token;
        localStorage.setItem('token', token);
        console.log('Login successful, token:', token);
        this.message = 'Login successful!';
        this.router.navigate(['/dashboard']);
      },
      error: (error) => {
        console.error('Login Error:', error);
        console.error('Error details:', error.error); // Log the error response body
        this.message = error.error?.message || 'Login failed! Please check your credentials.';
      },
    });
  }
}