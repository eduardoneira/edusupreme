package com.edusupreme.tournament.application.port.in;

public interface ListTournamentsUseCase {

    ListTournamentsResult listTournaments(int page, int size);
}
