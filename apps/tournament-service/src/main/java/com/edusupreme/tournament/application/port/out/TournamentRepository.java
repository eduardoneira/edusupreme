package com.edusupreme.tournament.application.port.out;

import com.edusupreme.tournament.domain.model.Tournament;

public interface TournamentRepository {

    Tournament save(Tournament tournament);
}
