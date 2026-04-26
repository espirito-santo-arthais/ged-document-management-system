import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { finalize } from 'rxjs';
import { LoadingService } from '../services/loading/loading.service';

export const loadingInterceptor: HttpInterceptorFn = (req, next) => {
    const loadingService = inject(LoadingService);

    // Notifica o serviço que uma nova requisição começou
    loadingService.show();

    return next(req).pipe(
        // O finalize é disparado quando a requisição termina (Sucesso ou Erro)
        finalize(() => {
            loadingService.hide();
        })
    );
};