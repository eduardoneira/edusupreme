package com.edusupreme.tournament.persistence;

import com.edusupreme.tournament.domain.model.Tournament;
import com.edusupreme.tournament.domain.model.TournamentStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "tournaments")
class TournamentJpaEntity {

    @Id
    private UUID id;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private TournamentStatus status;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    protected TournamentJpaEntity() {}

    private TournamentJpaEntity(
            UUID id,
            String name,
            LocalDate startDate,
            LocalDate endDate,
            TournamentStatus status,
            Instant createdAt) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.createdAt = createdAt;
    }

    static TournamentJpaEntity from(Tournament tournament) {
        return new TournamentJpaEntity(
                tournament.id(),
                tournament.name(),
                tournament.startDate(),
                tournament.endDate(),
                tournament.status(),
                tournament.createdAt());
    }

    Tournament toDomain() {
        return new Tournament(id, name, startDate, endDate, status, createdAt);
    }
}
