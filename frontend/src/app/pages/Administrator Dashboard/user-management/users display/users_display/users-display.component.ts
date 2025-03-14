import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Services } from './services/services.service';
import { SidebarComponent } from '../../../../../components/sidebar/sidebar.component';
import { Router } from '@angular/router';
import { Subscription, interval } from 'rxjs';

interface User {
    id: string;
    name: string;
    email: string;
    role: string;
    phoneNumber: string;
    profilePicture: string;
    status: string; // "ONLINE", "OFFLINE", "DEACTIVATED"
    lastActive: string;
    createdAt: string;
    updatedAt: string;
    activities: any[];
}

@Component({
    selector: 'app-users-display',
    templateUrl: './users-display.component.html',
    styleUrls: ['./users-display.component.css'],
    standalone: true,
    imports: [CommonModule, SidebarComponent, FormsModule]
})
export class UsersDisplayComponent implements OnInit, OnDestroy {
    users: User[] = [];
    errorMessage: string = '';
    selectedUser: User | null = null;
    changingPasswordUser: User | null = null;
    newPassword: string = '';
    confirmPassword: string = '';
    passwordError: string = '';
    passwordMatchMessage: string = '';
    private pollingSubscription: Subscription | null = null;

    constructor(private services: Services, private router: Router) {}

    ngOnInit(): void {
        this.fetchUsers();
        // Poll every 5 seconds for real-time updates
        this.pollingSubscription = interval(5000).subscribe(() => {
            this.fetchUsers();
        });
    }

    ngOnDestroy(): void {
        if (this.pollingSubscription) {
            this.pollingSubscription.unsubscribe();
        }
    }

    fetchUsers(): void {
        this.services.get<User[]>('users').subscribe({
            next: (users: User[]) => {
                this.users = users.map((user: User) => ({
                    ...user,
                    lastActive: new Date(user.lastActive).toISOString(),
                    createdAt: new Date(user.createdAt).toISOString(),
                    updatedAt: new Date(user.updatedAt).toISOString()
                }));
                console.log('Fetched users:', this.users.map(u => ({ email: u.email, status: u.status, lastActive: u.lastActive })));
            },
            error: (error: any) => {
                this.errorMessage = error.message || 'Failed to load users';
                console.error('Fetch users error:', error);
            }
        });
    }

    viewDetails(user: User): void {
        this.selectedUser = user;
    }

    closeModal(): void {
        this.selectedUser = null;
    }

    toggleUserActivation(user: User): void {
        if (user.status === 'DEACTIVATED') {
            this.reactivateUser(user);
        } else {
            this.deactivateUser(user);
        }
    }

    deactivateUser(user: User): void {
        if (confirm(`Are you sure you want to deactivate ${user.email}?`)) {
            this.services.post(`users/${user.id}/deactivate`, {}).subscribe({
                next: () => {
                    user.status = 'DEACTIVATED';
                    this.errorMessage = `User ${user.name || user.email} deactivated successfully.`;
                    this.fetchUsers(); // Refresh to ensure consistency
                },
                error: (error: any) => {
                    this.errorMessage = error.message || 'Failed to deactivate user.';
                }
            });
        }
    }

    reactivateUser(user: User): void {
        if (confirm(`Are you sure you want to reactivate ${user.email}?`)) {
            this.services.post(`users/${user.id}/reactivate`, {}).subscribe({
                next: () => {
                    user.status = 'OFFLINE'; // Set to OFFLINE until user logs in
                    this.errorMessage = `User ${user.name || user.email} reactivated successfully.`;
                    this.fetchUsers(); // Refresh to ensure consistency
                },
                error: (error: any) => {
                    this.errorMessage = error.message || 'Failed to reactivate user.';
                }
            });
        }
    }

    openChangePasswordModal(user: User): void {
        this.changingPasswordUser = user;
        this.newPassword = '';
        this.confirmPassword = '';
        this.passwordError = '';
        this.passwordMatchMessage = '';
    }

    closeChangePasswordModal(): void {
        this.changingPasswordUser = null;
    }

    validatePasswords(): void {
        if (this.newPassword && this.confirmPassword) {
            if (this.newPassword !== this.confirmPassword) {
                this.passwordError = 'Passwords do not match';
                this.passwordMatchMessage = '';
            } else if (this.newPassword.length < 8) {
                this.passwordError = 'Password must be at least 8 characters long';
                this.passwordMatchMessage = '';
            } else {
                this.passwordError = '';
                this.passwordMatchMessage = 'Passwords match!';
            }
        } else {
            this.passwordError = '';
            this.passwordMatchMessage = '';
        }
    }

    submitPasswordChange(): void {
        if (this.newPassword !== this.confirmPassword) {
            this.passwordError = 'Passwords do not match';
            return;
        }
        if (this.newPassword.length < 8) {
            this.passwordError = 'Password must be at least 8 characters long';
            return;
        }

        if (this.changingPasswordUser) {
            this.services.post(`users/${this.changingPasswordUser.id}/change-password`, { password: this.newPassword }).subscribe({
                next: () => {
                    this.errorMessage = `Password for ${this.changingPasswordUser?.name || this.changingPasswordUser?.email} updated successfully.`;
                    this.closeChangePasswordModal();
                    this.fetchUsers();
                },
                error: (error: any) => {
                    this.passwordError = error.message || 'Failed to change password.';
                }
            });
        }
    }

    navigateToAddUser(): void {
        this.router.navigate(['/admin/add-user']);
    }
}