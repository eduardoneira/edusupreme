package com.edusupreme.tournament.application.usecase;

import com.edusupreme.tournament.application.port.in.CreateTournamentCommand;
import com.edusupreme.tournament.application.port.in.CreateTournamentUseCase;
import com.edusupreme.tournament.application.port.out.TournamentRepository;
import com.edusupreme.tournament.domain.model.Tournament;
import java.time.Clock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateTournamentHandler implements CreateTournamentUseCase {

    private final TournamentRepository tournamentRepository;
    private final Clock clock;

    public CreateTournamentHandler(TournamentRepository tournamentRepository, Clock clock) {
        this.tournamentRepository = tournamentRepository;
        this.clock = clock;
    }

    @Override
    @Transactional
    public Tournament createTournament(CreateTournamentCommand command) {
        Tournament tournament = Tournament.create(
                command.name(),
                command.startDate(),
                command.endDate(),
                command.status(),
                clock.instant());

        return tournamentRepository.save(tournament);
    }
}
