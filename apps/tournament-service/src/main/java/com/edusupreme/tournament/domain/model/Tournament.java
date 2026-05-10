package com.edusupreme.tournament.domain.model;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public record Tournament(
        UUID id,
        String name,
        LocalDate startDate,
        LocalDate endDate,
        TournamentStatus status,
        Instant createdAt) {

    public Tournament {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(startDate, "startDate must not be null");
        Objects.requireNonNull(endDate, "endDate must not be null");
        Objects.requireNonNull(status, "status must not be null");
        Objects.requireNonNull(createdAt, "createdAt must not be null");

        name = normalizeName(name);
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("endDate must be on or after startDate");
        }
    }

    public static Tournament create(
            String name,
            LocalDate startDate,
            LocalDate endDate,
            TournamentStatus status,
            Instant createdAt) {
        return new Tournament(UUID.randomUUID(), name, startDate, endDate, status, createdAt);
    }

    private static String normalizeName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("name must not be blank");
        }

        String normalizedName = name.trim();
        if (normalizedName.length() < 3 || normalizedName.length() > 120) {
            throw new IllegalArgumentException("name must be between 3 and 120 characters");
        }

        return normalizedName;
    }
}
