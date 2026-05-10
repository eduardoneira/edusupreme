package com.edusupreme.tournament.persistence;

import com.edusupreme.tournament.application.port.out.TournamentRepository;
import com.edusupreme.tournament.domain.model.Tournament;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Repository
class JpaTournamentRepository implements TournamentRepository {

    private final SpringDataTournamentJpaRepository springDataRepository;

    JpaTournamentRepository(SpringDataTournamentJpaRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public List<Tournament> findAllNewestFirst(int page, int size) {
        PageRequest pageRequest = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "createdAt").and(Sort.by(Sort.Direction.DESC, "id")));

        return springDataRepository.findAll(pageRequest).stream()
                .map(TournamentJpaEntity::toDomain)
                .toList();
    }

    @Override
    public long count() {
        return springDataRepository.count();
    }

    @Override
    public Tournament save(Tournament tournament) {
        return springDataRepository.save(TournamentJpaEntity.from(tournament)).toDomain();
    }
}
