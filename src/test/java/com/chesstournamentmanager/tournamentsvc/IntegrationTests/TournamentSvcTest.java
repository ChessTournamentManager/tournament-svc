package com.chesstournamentmanager.tournamentsvc.IntegrationTests;

import com.chesstournamentmanager.tournamentsvc.config.TournamentData;
import com.chesstournamentmanager.tournamentsvc.controllers.TournamentController;
import com.chesstournamentmanager.tournamentsvc.models.Tournament;
import com.chesstournamentmanager.tournamentsvc.repositories.TournamentRepository;
import com.chesstournamentmanager.tournamentsvc.services.TournamentService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class TournamentSvcTest {

    private TournamentController tournamentController;
    private TournamentService tournamentService;
    @Mock
    private TournamentRepository tournamentRepository;
    private TournamentData tournamentData;
    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        tournamentService = new TournamentService(tournamentRepository);
        tournamentController = new TournamentController(tournamentService);
        tournamentData = new TournamentData();
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void GetsAllTournaments() {
        // Arrange
        Iterable<Tournament> givenTournaments = tournamentData.getTournaments();
        // Act
        when(tournamentRepository.findAll()).thenReturn(givenTournaments);
        Iterable<Tournament> tournaments = tournamentController.getTournaments();
        // Assert
        assertEquals(givenTournaments.toString(), tournaments.toString());
    }
}
