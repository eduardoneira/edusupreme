package com.edusupreme.tournament.application.port.in;

import com.edusupreme.tournament.domain.model.Tournament;
import java.util.List;

public record ListTournamentsResult(
        List<Tournament> items,
        int page,
        int size,
        long totalItems) {}
