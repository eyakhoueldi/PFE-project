import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, RouterLinkActive, Router } from '@angular/router';
import { Services } from '../../pages/Administrator Dashboard/user-management/users display/users_display/services/services.service'; // Verify this path

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [CommonModule, RouterLink, RouterLinkActive],
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.css']
})
export class SidebarComponent {
  @Input() role: 'ADMINISTRATOR' | 'PROJECT_MANAGER' | 'DEV_TEAM_MEMBER' = 'ADMINISTRATOR'; // Default role

  constructor(private router: Router, private services: Services) {} // Inject Services for API call

  onLogout() {
    this.services.logout().subscribe({
      next: (response: any) => { // Explicitly type response as any (or refine if you know the shape)
        console.log('Logout successful:', response); // Log success
        localStorage.clear(); // Clear all localStorage data (including token)
        this.router.navigate(['/login']); // Redirect to login page
      },
      error: (error: any) => { // Explicitly type error as any (or refine if you know the shape)
        console.error('Logout failed:', error); // Log error
        localStorage.clear(); // Clear localStorage even if backend call fails
        this.router.navigate(['/login']); // Redirect anyway
      }
    });
  }
}