import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';

export const routes: Routes = [
    {
        path: '',
        redirectTo: 'home',
        pathMatch: 'full'
    },
    {
        path: 'home',
        data: { showFooter: true },
        loadComponent: () =>
            import('./features/home/home.component')
                .then(m => m.HomeComponent)
    },
    {
        path: 'login',
        data: { showFooter: true },
        loadComponent: () =>
            import('./features/auth/login/login.component')
                .then(m => m.LoginComponent)
    },
    {
        path: 'dashboard',
        canActivate: [authGuard],
        data: { showFooter: true },
        loadComponent: () =>
            import('./features/dashboard/dashboard.component')
                .then(m => m.DashboardComponent)
    },
    {
        path: 'documents',
        canActivate: [authGuard],
        data: { showFooter: false },
        loadComponent: () =>
            import('./features/document-list/document-list.component')
                .then(m => m.DocumentListComponent)
    }
];