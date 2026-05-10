package com.edusupreme.tournament.persistence;

import com.edusupreme.tournament.application.port.out.TournamentRepository;
import com.edusupreme.tournament.domain.model.Tournament;
import org.springframework.stereotype.Repository;

@Repository
class JpaTournamentRepository implements TournamentRepository {

    private final SpringDataTournamentJpaRepository springDataRepository;

    JpaTournamentRepository(SpringDataTournamentJpaRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public Tournament save(Tournament tournament) {
        return springDataRepository.save(TournamentJpaEntity.from(tournament)).toDomain();
    }
}
