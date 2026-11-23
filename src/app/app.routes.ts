import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () => import('./features/system/pages/home-page/home-page').then(m => m.HomePage),
    title: 'Reservas de laboratorios'
  },
  { path: '**', redirectTo: '' }
];
