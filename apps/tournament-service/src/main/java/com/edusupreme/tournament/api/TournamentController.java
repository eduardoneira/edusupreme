package com.edusupreme.tournament.api;

import com.edusupreme.tournament.application.port.in.CreateTournamentCommand;
import com.edusupreme.tournament.application.port.in.CreateTournamentUseCase;
import com.edusupreme.tournament.application.port.in.ListTournamentsUseCase;
import com.edusupreme.tournament.domain.model.Tournament;
import com.edusupreme.tournament.domain.model.TournamentStatus;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tournaments")
@Validated
public class TournamentController {

    private final CreateTournamentUseCase createTournamentUseCase;
    private final ListTournamentsUseCase listTournamentsUseCase;

    public TournamentController(
            CreateTournamentUseCase createTournamentUseCase,
            ListTournamentsUseCase listTournamentsUseCase) {
        this.createTournamentUseCase = createTournamentUseCase;
        this.listTournamentsUseCase = listTournamentsUseCase;
    }

    @GetMapping
    ResponseEntity<ListTournamentsResponse> listTournaments(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size) {
        return ResponseEntity.ok(ListTournamentsResponse.from(listTournamentsUseCase.listTournaments(page, size)));
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
