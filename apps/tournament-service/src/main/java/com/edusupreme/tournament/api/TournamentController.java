package com.edusupreme.tournament.api;

import com.edusupreme.tournament.application.port.in.CreateTournamentCommand;
import com.edusupreme.tournament.application.port.in.CreateTournamentUseCase;
import com.edusupreme.tournament.domain.model.Tournament;
import com.edusupreme.tournament.domain.model.TournamentStatus;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tournaments")
public class TournamentController {

    private final CreateTournamentUseCase createTournamentUseCase;

    public TournamentController(CreateTournamentUseCase createTournamentUseCase) {
        this.createTournamentUseCase = createTournamentUseCase;
    }

    @PostMapping
    ResponseEntity<TournamentResponse> createTournament(@Valid @RequestBody CreateTournamentRequest request) {
        Tournament tournament = createTournamentUseCase.createTournament(new CreateTournamentCommand(
                request.name(),
                request.startDate(),
                request.endDate(),
                request.status() == null ? TournamentStatus.DRAFT : request.status()));

        return ResponseEntity.created(URI.create("/tournaments/" + tournament.id()))
                .body(TournamentResponse.from(tournament));
    }
}
