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

    const validationError = this.validateForm();

    if (validationError) {
      this.errorMessage = validationError;
      return;
    }

    this.authService.login({ username: this.username, password: this.password }).subscribe({
      next: () => {
        this.router.navigate(['/dashboard']);
      },
      error: (err: any) => {
        this.errorMessage = err.userMessage || 'Erro ao autenticar.';
      }
    });
  }

  private validateForm(): string | null {

    if (!this.username && !this.password) {
      return 'E-mail e senha são obrigatórios';
    }

    if (!this.username) {
      return 'E-mail é obrigatório';
    }

    if (!this.password) {
      return 'Senha é obrigatória';
    }

    if (!this.username.includes('@')) {
      return 'Informe um e-mail válido';
    }

    return null;
  }

}