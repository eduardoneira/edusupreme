package com.edusupreme.tournament.api;

import com.edusupreme.tournament.domain.model.Tournament;
import com.edusupreme.tournament.domain.model.TournamentStatus;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record TournamentResponse(
        UUID id,
        String name,
        LocalDate startDate,
        LocalDate endDate,
        TournamentStatus status,
        Instant createdAt) {

    static TournamentResponse from(Tournament tournament) {
        return new TournamentResponse(
                tournament.id(),
                tournament.name(),
                tournament.startDate(),
                tournament.endDate(),
                tournament.status(),
                tournament.createdAt());
    }
}
