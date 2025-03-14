// src/app/app.routes.ts
import { Routes } from '@angular/router';

import { LoginComponent } from './pages/login/login.component';
import { AdminDashboardComponent } from './pages/Administrator Dashboard/admin-dashboard/admin-dashboard.component';
import { UserRegistrationComponent } from './pages/Administrator Dashboard/user-management/user-registration/user-registration.component';
import { UsersDisplayComponent } from './pages/Administrator Dashboard/user-management/users display/users_display/users-display.component';
export const routes: Routes = [
  {
    path: 'login',
    component: LoginComponent,
  },
  {
    path: 'admin',
    children: [
      {
        path: 'dashboard',
        component: AdminDashboardComponent,
      },
      {
        path: 'add-user',
        component: UserRegistrationComponent,
      },
      {
        path: 'users-display',
        component: UsersDisplayComponent,
      },
      {
        path: '',
        redirectTo: 'dashboard',
        pathMatch: 'full',
      },
    ],
  },
  {
    path: '',
    redirectTo: 'login', // Redirect to login page by default
    pathMatch: 'full',
  },
  {
    path: '**',
    redirectTo: 'login', // Redirect to login page for unknown routes
  },
];