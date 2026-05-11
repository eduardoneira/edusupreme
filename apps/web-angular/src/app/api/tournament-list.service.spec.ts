import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { provideApi } from './generated/tournament-service';
import { TournamentListService } from './tournament-list.service';

describe('TournamentListService', () => {
  let http: HttpTestingController;
  let service: TournamentListService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        provideApi('http://localhost:8081'),
        TournamentListService,
      ],
    });

    http = TestBed.inject(HttpTestingController);
    service = TestBed.inject(TournamentListService);
  });

  afterEach(() => {
    http.verify();
  });

  it('lists tournaments through the generated API client', () => {
    const receivedItems: unknown[] = [];

    service.listLatest().subscribe((items) => receivedItems.push(...items));

    const request = http.expectOne((candidate) => {
      return (
        candidate.method === 'GET' &&
        candidate.url === 'http://localhost:8081/tournaments' &&
        candidate.params.get('page') === '0' &&
        candidate.params.get('size') === '3'
      );
    });

    request.flush({
      items: [
        {
          id: '018f3904-09fb-7d19-baf4-3d10a2f0bb99',
          name: 'Madrid Spring Open',
          startDate: '2026-06-20',
          endDate: '2026-06-21',
          status: 'SCHEDULED',
          createdAt: '2026-05-07T10:15:30Z',
        },
      ],
      page: 0,
      size: 3,
      totalItems: 1,
    });

    expect(receivedItems).toEqual([
      {
        dateRange: 'Jun 20 - Jun 21',
        id: '018f3904-09fb-7d19-baf4-3d10a2f0bb99',
        name: 'Madrid Spring Open',
        status: 'SCHEDULED',
      },
    ]);
  });
});
