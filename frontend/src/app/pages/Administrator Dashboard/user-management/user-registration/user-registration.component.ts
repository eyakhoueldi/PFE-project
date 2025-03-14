import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';  // Import Router
import { UserService } from './services/services.service';
import { UserRole } from '../../../../user-role.enum';
import { SidebarComponent } from '../../../../components/sidebar/sidebar.component';

@Component({
  selector: 'app-user-registration',
  standalone: true,
  imports: [FormsModule, CommonModule, SidebarComponent],
  templateUrl: './user-registration.component.html',
  styleUrls: ['./user-registration.component.css'],
})
export class UserRegistrationComponent {
  email: string = '';
  password: string = '';
  role: UserRole = UserRole.ADMINISTRATOR;
  message: string = '';
  isError: boolean = false;
  roleOptions: UserRole[] = Object.values(UserRole);

  constructor(
    private userService: UserService,
    private router: Router  // Inject Router
  ) {}

  onSubmit() {
    this.userService.createUser(this.email, this.password, this.role).subscribe({
      next: (response) => {
        this.message = 'User created successfully!';
        this.isError = false;
        this.resetForm();
      },
      error: (error) => {
        this.message = 'Error creating user: ' + error.error.message;
        this.isError = true;
      },
    });
  }

  resetForm() {
    this.email = '';
    this.password = '';
    this.role = UserRole.ADMINISTRATOR;
  }

  goBack() {
    this.router.navigate(['/admin/users-display']);  // Navigate to parent route
    // OR specify a specific route like: this.router.navigate(['/previous-page']);
  }
}