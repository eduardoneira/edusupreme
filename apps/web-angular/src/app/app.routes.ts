import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: 'dashboard',
    loadComponent: () =>
      import('./features/dashboard/dashboard').then((component) => component.Dashboard),
  },
  {
    path: 'tournaments',
    loadComponent: () =>
      import('./features/feature-placeholder/feature-placeholder').then(
        (component) => component.FeaturePlaceholder,
      ),
    data: {
      title: 'Tournaments',
      description: 'Tournament list, creation, and organizer workflows will land here.',
    },
  },
  {
    path: 'players',
    loadComponent: () =>
      import('./features/feature-placeholder/feature-placeholder').then(
        (component) => component.FeaturePlaceholder,
      ),
    data: {
      title: 'Players',
      description: 'Player registration, seed review, and roster management will land here.',
    },
  },
  {
    path: 'matches',
    loadComponent: () =>
      import('./features/feature-placeholder/feature-placeholder').then(
        (component) => component.FeaturePlaceholder,
      ),
    data: {
      title: 'Matches',
      description: 'Match scheduling, table assignment, and scoring workflows will land here.',
    },
  },
  {
    path: 'standings',
    loadComponent: () =>
      import('./features/feature-placeholder/feature-placeholder').then(
        (component) => component.FeaturePlaceholder,
      ),
    data: {
      title: 'Standings',
      description: 'Group standings, bracket progress, and ranking views will land here.',
    },
  },
  {
    path: '',
    pathMatch: 'full',
    redirectTo: 'dashboard',
  },
  {
    path: '**',
    redirectTo: 'dashboard',
  },
];
