package com.edusupreme.tournament.api;

import com.edusupreme.tournament.domain.model.TournamentStatus;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record CreateTournamentRequest(
        @NotBlank @Size(min = 3, max = 120) String name,
        @NotNull LocalDate startDate,
        @NotNull LocalDate endDate,
        TournamentStatus status) {

    @AssertTrue(message = "endDate must be on or after startDate")
    boolean hasValidDateRange() {
        return startDate == null || endDate == null || !endDate.isBefore(startDate);
    }
}
