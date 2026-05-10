package com.edusupreme.tournament.persistence;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

interface SpringDataTournamentJpaRepository extends JpaRepository<TournamentJpaEntity, UUID> {}
