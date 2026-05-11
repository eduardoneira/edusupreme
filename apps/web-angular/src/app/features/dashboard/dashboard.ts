import { Component, computed, inject } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { RouterLink } from '@angular/router';
import { catchError, map, of, startWith } from 'rxjs';

import { TournamentListItem, TournamentListService } from '../../api/tournament-list.service';

interface TournamentListState {
  items: TournamentListItem[];
  status: 'error' | 'loading' | 'ready';
}

@Component({
  selector: 'app-dashboard',
  imports: [RouterLink],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.scss',
})
export class Dashboard {
  private readonly tournamentListService = inject(TournamentListService);

  private readonly tournamentListState = toSignal(
    this.tournamentListService.listLatest().pipe(
      map((items): TournamentListState => ({ items, status: 'ready' })),
      startWith<TournamentListState>({ items: [], status: 'loading' }),
      catchError(() => of<TournamentListState>({ items: [], status: 'error' })),
    ),
    { initialValue: { items: [], status: 'loading' } satisfies TournamentListState },
  );

  protected readonly tournamentStatus = computed(() => this.tournamentListState().status);
  protected readonly tournaments = computed(() => this.tournamentListState().items);

  protected readonly metrics = [
    { label: 'Active tournaments', value: '3', detail: '2 accepting registrations' },
    { label: 'Players registered', value: '148', detail: '32 seeded for review' },
    { label: 'Matches scheduled', value: '64', detail: '18 pending table assignment' },
    { label: 'Live tables', value: '12', detail: '4 entering results' },
  ];

  protected readonly matches = [
    { table: 'Table 1', round: 'Group A', players: 'M. Silva vs A. Ramos', score: '2-1' },
    { table: 'Table 4', round: 'Group C', players: 'N. Vega vs I. Costa', score: '0-0' },
    { table: 'Table 8', round: 'Quarterfinal', players: 'L. Park vs C. Diaz', score: '1-1' },
  ];
}
