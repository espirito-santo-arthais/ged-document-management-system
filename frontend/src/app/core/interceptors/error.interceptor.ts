import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { catchError, throwError } from 'rxjs';

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
    return next(req).pipe(
        catchError((error: HttpErrorResponse) => {

            // Se for 401, não fazemos nada aqui. 
            // Deixamos o erro original seguir para o authInterceptor tratar.
            if (error.status === 401) {
                return throwError(() => error);
            }

            let errorMessage = 'Ocorreu um erro inesperado.';

            if (error.status === 0) {
                errorMessage = 'Não foi possível conectar ao servidor.';
            } else if (error.status === 400) {

                const backendMessage = error?.error?.message;

                if (backendMessage?.includes('username') && backendMessage?.includes('password')) {
                    errorMessage = 'E-mail e senha são obrigatórios.';
                }
                else if (backendMessage?.includes('username')) {
                    errorMessage = 'E-mail é obrigatório.';
                }
                else if (backendMessage?.includes('password')) {
                    errorMessage = 'Senha é obrigatória.';
                }
                else {
                    errorMessage = 'Dados informados são inválidos.';
                }
            } else if (error.status === 403) {
                errorMessage = 'Você não tem permissão para realizar esta ação.';
            } else if (error.status >= 400 && error.status < 500) {
                errorMessage = error.error?.message || 'Requisição inválida ou mal formatada.';
            } else if (error.status >= 500) {
                errorMessage = 'Erro interno no servidor. Tente novamente mais tarde.';
            }

            return throwError(() => new Error(errorMessage));
        })
    );
};