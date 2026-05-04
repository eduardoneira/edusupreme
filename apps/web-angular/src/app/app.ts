import { Component } from '@angular/core';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-root',
  imports: [RouterLink, RouterLinkActive, RouterOutlet],
  templateUrl: './app.html',
  styleUrl: './app.scss',
})
export class App {
  protected readonly primaryNavigation = [
    { label: 'Dashboard', path: '/dashboard' },
    { label: 'Tournaments', path: '/tournaments' },
    { label: 'Players', path: '/players' },
    { label: 'Matches', path: '/matches' },
    { label: 'Standings', path: '/standings' },
  ];
}
