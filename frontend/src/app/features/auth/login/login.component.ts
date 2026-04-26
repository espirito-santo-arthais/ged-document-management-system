import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { AuthService } from '../../../core/services/auth/auth.service';
import { LoadingService } from '../../../core/services/loading/loading.service'; 

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css',
})
export class LoginComponent {
  private authService = inject(AuthService);
  private router = inject(Router);
  // Expondo o loadingService para o template
  protected loadingService = inject(LoadingService);

  username = '';
  password = '';
  errorMessage = '';

  onSubmit() {
    this.errorMessage = '';

    this.authService.login({ username: this.username, password: this.password }).subscribe({
      next: () => {
        this.router.navigate(['/home']);
      },
      error: (err: Error) => {
        this.errorMessage = err.message;
      }
    });
  }
}