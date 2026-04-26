import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http'; // 1. Importação necessária
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, EMPTY, throwError } from 'rxjs';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const router = inject(Router);
  const token = localStorage.getItem('token');

  // Clone da requisição com o Header
  const clonedRequest = token && !req.url.includes('/auth/login')
    ? req.clone({ setHeaders: { Authorization: `Bearer ${token}` } })
    : req;

  return next(clonedRequest).pipe(
    catchError((error: unknown) => { // 2. Tipagem segura
      if (error instanceof HttpErrorResponse) { // 3. Verificação de instância
        if (error.status === 401) {
          console.warn('Sessão expirada. Redirecionando para login...');

          // Limpa o estado
          localStorage.removeItem('token');
          
          // Use navigateByUrl para garantir que saia de qualquer rota filha
          // e adicione um then() para debug se necessário
          router.navigateByUrl('/login').then(nav => {
            if (!nav) console.error('Falha ao navegar para login');
          });

          // IMPORTANTE: Retornamos um Observable vazio para "matar" a requisição
          // e não deixar o erro chegar no subscribe do seu componente.
          return EMPTY;
        }
      }
      return throwError(() => error);
    })
  );
};