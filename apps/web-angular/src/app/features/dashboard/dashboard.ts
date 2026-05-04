import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-dashboard',
  imports: [RouterLink],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.scss',
})
export class Dashboard {
  protected readonly metrics = [
    { label: 'Active tournaments', value: '3', detail: '2 accepting registrations' },
    { label: 'Players registered', value: '148', detail: '32 seeded for review' },
    { label: 'Matches scheduled', value: '64', detail: '18 pending table assignment' },
    { label: 'Live tables', value: '12', detail: '4 entering results' },
  ];

  protected readonly tournaments = [
    { name: 'Spring Open', status: 'Registration', date: 'May 18', players: 64 },
    { name: 'Club Ladder Finals', status: 'Scheduling', date: 'May 24', players: 32 },
    { name: 'Junior Circuit', status: 'Draft', date: 'June 01', players: 52 },
  ];

  protected readonly matches = [
    { table: 'Table 1', round: 'Group A', players: 'M. Silva vs A. Ramos', score: '2-1' },
    { table: 'Table 4', round: 'Group C', players: 'N. Vega vs I. Costa', score: '0-0' },
    { table: 'Table 8', round: 'Quarterfinal', players: 'L. Park vs C. Diaz', score: '1-1' },
  ];
}
