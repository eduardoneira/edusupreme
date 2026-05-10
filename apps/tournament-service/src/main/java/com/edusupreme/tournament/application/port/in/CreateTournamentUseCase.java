package com.edusupreme.tournament.application.port.in;

import com.edusupreme.tournament.domain.model.Tournament;

public interface CreateTournamentUseCase {

    Tournament createTournament(CreateTournamentCommand command);
}
