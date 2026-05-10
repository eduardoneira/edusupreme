package com.edusupreme.tournament.api;

import com.edusupreme.tournament.application.port.in.ListTournamentsResult;
import java.util.List;

public record ListTournamentsResponse(
        List<TournamentResponse> items,
        int page,
        int size,
        long totalItems) {

    static ListTournamentsResponse from(ListTournamentsResult result) {
        return new ListTournamentsResponse(
                result.items().stream()
                        .map(TournamentResponse::from)
                        .toList(),
                result.page(),
                result.size(),
                result.totalItems());
    }
}
