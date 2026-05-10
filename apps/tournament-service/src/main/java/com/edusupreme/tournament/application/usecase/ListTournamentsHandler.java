package com.edusupreme.tournament.application.usecase;

import com.edusupreme.tournament.application.port.in.ListTournamentsResult;
import com.edusupreme.tournament.application.port.in.ListTournamentsUseCase;
import com.edusupreme.tournament.application.port.out.TournamentRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ListTournamentsHandler implements ListTournamentsUseCase {

    private final TournamentRepository tournamentRepository;

    public ListTournamentsHandler(TournamentRepository tournamentRepository) {
        this.tournamentRepository = tournamentRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public ListTournamentsResult listTournaments(int page, int size) {
        return new ListTournamentsResult(
                List.copyOf(tournamentRepository.findAllNewestFirst(page, size)),
                page,
                size,
                tournamentRepository.count());
    }
}
