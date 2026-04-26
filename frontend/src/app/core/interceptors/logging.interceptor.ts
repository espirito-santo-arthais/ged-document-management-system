import { HttpInterceptorFn } from '@angular/common/http';
import { tap, finalize } from 'rxjs';
import { environment } from '../../../environments/environment';

export const loggingInterceptor: HttpInterceptorFn = (req, next) => {
    if (environment.production) return next(req);

    const startTime = Date.now();
    let status: string;

    return next(req).pipe(
        tap({
            next: () => (status = 'succeeded'),
            error: () => (status = 'failed'),
        }),
        finalize(() => {
            const elapsed = Date.now() - startTime;
            console.log(`[HTTP] ${req.method} ${req.urlWithParams} - ${status} em ${elapsed}ms`);
        })
    );
};