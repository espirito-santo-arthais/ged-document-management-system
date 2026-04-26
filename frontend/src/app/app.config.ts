import { ApplicationConfig, provideBrowserGlobalErrorListeners } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { routes } from './app.routes';
import { loadingInterceptor } from './core/interceptors/loading.interceptor';
import { loggingInterceptor } from './core/interceptors/logging.interceptor';
import { authInterceptor } from './core/interceptors/auth.interceptor';
import { errorInterceptor } from './core/interceptors/error.interceptor';

export const appConfig: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideRouter(routes),
    provideHttpClient(withInterceptors([
      // Adicione o loadingInterceptor no início da fila.
      // É importante que ele seja um dos primeiros para
      // capturar o tempo total, incluindo o processamento
      // dos outros interceptores.
      loadingInterceptor,
      loggingInterceptor,
      authInterceptor,
      errorInterceptor
    ]))
  ]
};
