package com.edusupreme.tournament.application.port.out;

import com.edusupreme.tournament.domain.model.Tournament;
import java.util.List;

public interface TournamentRepository {

    List<Tournament> findAllNewestFirst(int page, int size);

    long count();

    Tournament save(Tournament tournament);
}
