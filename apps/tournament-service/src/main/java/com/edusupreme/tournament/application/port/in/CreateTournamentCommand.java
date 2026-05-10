package com.edusupreme.tournament.application.port.in;

import com.edusupreme.tournament.domain.model.TournamentStatus;
import java.time.LocalDate;

public record CreateTournamentCommand(
        String name,
        LocalDate startDate,
        LocalDate endDate,
        TournamentStatus status) {}
