package com.chesstournamentmanager.tournamentsvc.UnitTests.services;

import com.chesstournamentmanager.tournamentsvc.config.TournamentData;
import com.chesstournamentmanager.tournamentsvc.controllers.TournamentController;
import com.chesstournamentmanager.tournamentsvc.models.RequestModel;
import com.chesstournamentmanager.tournamentsvc.models.Tournament;
import com.chesstournamentmanager.tournamentsvc.services.TournamentService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TournamentControllerTest {

    @Mock
    private TournamentService tournamentService;
    private AutoCloseable autoCloseable;
    private TournamentController tournamentController;
    private TournamentData tournamentData;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        tournamentController = new TournamentController(tournamentService);
        tournamentData = new TournamentData();
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void GetsAllTournaments() {
        // Act
        tournamentController.getTournaments();
        // Assert
        verify(tournamentService).getTournaments();
    }

    @Test
    void GetsOneTournament() {
        // Arrange
        Tournament tournament = tournamentData.getTournaments().get(0);
        // Act
        when(tournamentService.getTournament(tournament.getId())).thenReturn(Optional.of(tournament));
        tournamentController.getTournament(tournament.getId());
        // Assert
        verify(tournamentService).getTournament(tournament.getId());
    }

    @Test
    void AddsANewTournament() {

        // Arrange
        Tournament tournament = tournamentData.getTournaments().get(0);
        RequestModel requestModel = new RequestModel(
                tournament.getHostId(),
                tournament.getName(),
                tournament.getStartTime(),
                tournament.getMaxRounds(),
                tournament.getTimePerPlayer()
        );

        // Act
        when(tournamentService.tournamentValidation(isA(Tournament.class))).thenReturn("");
        when(tournamentService.getTournament(isA(UUID.class))).thenReturn(Optional.of(tournament));
        tournamentController.addTournament(requestModel);

        // Assert
        verify(tournamentService).tournamentValidation(isA(Tournament.class));
        verify(tournamentService).addNewTournament(isA(Tournament.class));
        verify(tournamentService).getTournament(isA(UUID.class));
    }
}
