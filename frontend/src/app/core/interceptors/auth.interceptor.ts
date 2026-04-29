import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http'; // 1. Importação necessária
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, EMPTY, throwError } from 'rxjs';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const router = inject(Router);
  const token = localStorage.getItem('token');

  const isAuthRequest = req.url.includes('/auth/login');

  // Clone da requisição com o Header
  const clonedRequest = token && !isAuthRequest
    ? req.clone({ setHeaders: { Authorization: `Bearer ${token}` } })
    : req;

  return next(clonedRequest).pipe(
    catchError((error: unknown) => {

      if (error instanceof HttpErrorResponse) {

        // TRATA O LOGIN DE FORMA DIFERENCIADA
        if (isAuthRequest) {

          let userMessage = 'Erro ao autenticar.';

          if (error.status === 0) {
            userMessage = 'Não foi possível conectar ao servidor.';
          } else if (error.status === 401) {
            userMessage = 'Usuário ou senha inválidos.';
          } else if (error.status >= 500) {
            userMessage = 'Erro interno no servidor. Tente novamente mais tarde.';
          }

          return throwError(() => ({
            ...error,
            userMessage
          }));
        }

        // TRATA SESSÃO EXPIRADA
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

        // fallback amigável (caso algo passe)
        let userMessage = 'Erro inesperado.';

        if (error.status === 0) {
          userMessage = 'Não foi possível conectar ao servidor.';
        } else if (error.status >= 500) {
          userMessage = 'Erro interno no servidor. Tente novamente mais tarde.';
        }

        return throwError(() => ({
          ...error,
          userMessage
        }));

      }

      return throwError(() => error);
    })
  );
};