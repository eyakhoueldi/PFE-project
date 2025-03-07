import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { SidebarComponent } from "./components/sidebar/sidebar.component";
import { LoginComponent } from "./pages/login/login.component";

@Component({
  selector: 'app-root',
  standalone: true, // Ensure this is a standalone component
  imports: [LoginComponent, SidebarComponent], // Add SidebarComponent here
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'] // Ensure Tailwind is correctly loaded here
})
export class AppComponent {
  title = 'frontend';
}
