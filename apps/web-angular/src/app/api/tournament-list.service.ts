import { inject, Injectable } from '@angular/core';
import { map, Observable } from 'rxjs';

import {
  TournamentResponse,
  TournamentsService,
} from './generated/tournament-service';

export interface TournamentListItem {
  dateRange: string;
  id: string;
  name: string;
  status: TournamentResponse['status'];
}

@Injectable({
  providedIn: 'root',
})
export class TournamentListService {
  private readonly tournamentsService = inject(TournamentsService);

  listLatest(limit = 3): Observable<TournamentListItem[]> {
    return this.tournamentsService.listTournaments({ page: 0, size: limit }).pipe(
      map((response) =>
        response.items.map((tournament) => ({
          dateRange: this.formatDateRange(tournament.startDate, tournament.endDate),
          id: tournament.id,
          name: tournament.name,
          status: tournament.status,
        })),
      ),
    );
  }

  private formatDateRange(startDate: string, endDate: string): string {
    const formatter = new Intl.DateTimeFormat('en', {
      day: '2-digit',
      month: 'short',
    });

    const start = formatter.format(new Date(`${startDate}T00:00:00`));
    const end = formatter.format(new Date(`${endDate}T00:00:00`));

    return start === end ? start : `${start} - ${end}`;
  }
}
