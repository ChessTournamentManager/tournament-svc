package com.chesstournamentmanager.tournamentsvc.UnitTests.services;

import com.chesstournamentmanager.tournamentsvc.config.TournamentData;
import com.chesstournamentmanager.tournamentsvc.models.Tournament;
import com.chesstournamentmanager.tournamentsvc.repositories.TournamentRepository;
import com.chesstournamentmanager.tournamentsvc.services.TournamentService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TournamentServiceTest {

    @Mock
    private TournamentRepository tournamentRepository;
    private AutoCloseable autoCloseable;
    private TournamentService tournamentService;
    private TournamentData tournamentData;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        tournamentService = new TournamentService(tournamentRepository);
        tournamentData = new TournamentData();
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void GetsAllTournaments() {
        // Act
        tournamentService.getTournaments();
        // Assert
        verify(tournamentRepository).findAll();
    }

    @Test
    void GetsOneTournament() {
        // Arrange
        UUID id = UUID.randomUUID();
        // Act
        tournamentService.getTournament(id);
        // Assert
        verify(tournamentRepository).findById(id);
    }

    @Test
    void AddsANewTournament() {
        // Arrange
        Tournament tournament = tournamentData.getTournaments().get(0);
        // Act
        tournamentService.addNewTournament(tournament);
        // Assert
        verify(tournamentRepository).save(tournament);
    }

    @Test
    void ThrowsExceptionWhenDeletingANonExistentTournament() {
        // Arrange
        Throwable thrown;
        UUID id = tournamentData.getTournaments().get(0).getId();
        // Assert
        thrown = assertThrows(IllegalStateException.class, () -> tournamentService.deleteTournament(id));
        assertEquals("Tournament with id " + id + " does not exist", thrown.getMessage());
    }

    @Test
    void DeletesATournament() {
        // Arrange
        UUID id = tournamentData.getTournaments().get(0).getId();
        // Act
        when(tournamentRepository.existsById(id)).thenReturn(true);
        tournamentService.deleteTournament(id);
        // Assert
        verify(tournamentRepository).existsById(id);
        verify(tournamentRepository).deleteById(id);
    }

    // VALIDATION

    @Test
    void VerifiesAValidTournament_ReturnsEmptyString() {
        // Arrange
        Tournament tournament = tournamentData.getTournaments().get(0);
        // Act
        String message = tournamentService.tournamentValidation(tournament);
        // Assert
        assertEquals("", message);
    }

    // Validation tests

    @Test
    void ValidatesATournament_WithNoHostID_ReturnsCorrespondingMessage() {
        // Arrange
        Tournament tournament = tournamentData.getTournaments().get(0);
        tournament.setHostId(null);
        // Act
        String message = tournamentService.tournamentValidation(tournament);
        // Assert
        assertEquals(TournamentService.MESSAGE_NO_HOST_ID, message);
    }

    @Test
    void ValidatesATournament_WithNoName_ReturnsCorrespondingMessage() {
        // Arrange
        Tournament tournament = tournamentData.getTournaments().get(0);
        tournament.setName(null);
        // Act
        String message = tournamentService.tournamentValidation(tournament);
        // Assert
        assertEquals(TournamentService.MESSAGE_NO_NAME, message);
    }

    @Test
    void ValidatesATournament_WithEmptyName_ReturnsCorrespondingMessage() {
        // Arrange
        Tournament tournament = tournamentData.getTournaments().get(0);
        tournament.setName("");
        // Act
        String message = tournamentService.tournamentValidation(tournament);
        // Assert
        assertEquals(TournamentService.MESSAGE_EMPTY_NAME, message);
    }

    @Test
    void ValidatesATournament_WithNoStatus_ReturnsCorrespondingMessage() {
        // Arrange
        Tournament tournament = tournamentData.getTournaments().get(1);
        tournament.setStatus(null);
        // Act
        String message = tournamentService.tournamentValidation(tournament);
        // Assert
        assertEquals(TournamentService.MESSAGE_NO_STATUS, message);
    }

    @Test
    void ValidatesATournament_WithPlannedStatusAndTimeOfPast_ReturnsCorrespondingMessage() {
        // Arrange
        Tournament tournament = tournamentData.getTournaments().get(1);
        tournament.setStatus(Tournament.Status.PLANNED);
        tournament.setStartTime(LocalDateTime.of(2018, Month.DECEMBER, 28, 14, 0, 0));
        // Act
        String message = tournamentService.tournamentValidation(tournament);
        // Assert
        assertEquals(TournamentService.MESSAGE_PLANNED_TOURNAMENT_STARTS_IN_PAST, message);
    }

    @Test
    void ValidatesATournament_WithOngoingStatusAndTimeOfFuture_ReturnsCorrespondingMessage() {
        // Arrange
        Tournament tournament = tournamentData.getTournaments().get(1);
        tournament.setStatus(Tournament.Status.ONGOING);
        tournament.setStartTime(LocalDateTime.of(2999, Month.DECEMBER, 28, 14, 0, 0));
        // Act
        String message = tournamentService.tournamentValidation(tournament);
        // Assert
        assertEquals(TournamentService.MESSAGE_NON_PLANNED_TOURNAMENT_STARTS_IN_FUTURE, message);
    }

    @Test
    void ValidatesATournament_WithOnHoldStatusAndTimeOfFuture_ReturnsCorrespondingMessage() {
        // Arrange
        Tournament tournament = tournamentData.getTournaments().get(1);
        tournament.setStatus(Tournament.Status.ON_HOLD);
        tournament.setStartTime(LocalDateTime.of(2999, Month.DECEMBER, 28, 14, 0, 0));
        // Act
        String message = tournamentService.tournamentValidation(tournament);
        // Assert
        assertEquals(TournamentService.MESSAGE_NON_PLANNED_TOURNAMENT_STARTS_IN_FUTURE, message);
    }

    @Test
    void ValidatesATournament_WithFinishedStatusAndTimeOfFuture_ReturnsCorrespondingMessage() {
        // Arrange
        Tournament tournament = tournamentData.getTournaments().get(1);
        tournament.setStatus(Tournament.Status.FINISHED);
        tournament.setStartTime(LocalDateTime.of(2999, Month.DECEMBER, 28, 14, 0, 0));
        // Act
        String message = tournamentService.tournamentValidation(tournament);
        // Assert
        assertEquals(TournamentService.MESSAGE_NON_PLANNED_TOURNAMENT_STARTS_IN_FUTURE, message);
    }

    @Test
    void ValidatesATournament_WithCancelledStatusAndTimeOfFuture_ReturnsEmptyString() {
        // Arrange
        Tournament tournament = tournamentData.getTournaments().get(1);
        tournament.setStatus(Tournament.Status.CANCELLED);
        tournament.setStartTime(LocalDateTime.of(2999, Month.DECEMBER, 28, 14, 0, 0));
        // Act
        String message = tournamentService.tournamentValidation(tournament);
        // Assert
        assertEquals("", message);
    }

    @Test
    void ValidatesATournament_WithNoStartTime_ReturnsCorrespondingMessage() {
        // Arrange
        Tournament tournament = tournamentData.getTournaments().get(2);
        tournament.setStartTime(null);
        // Act
        String message = tournamentService.tournamentValidation(tournament);
        // Assert
        assertEquals(TournamentService.MESSAGE_NO_START_TIME, message);
    }

    @Test
    void ValidatesATournament_WithNoMaxRounds_ReturnsCorrespondingMessage() {
        // Arrange
        Tournament tournament = tournamentData.getTournaments().get(2);
        tournament.setMaxRounds(0);
        // Act
        String message = tournamentService.tournamentValidation(tournament);
        // Assert
        assertEquals(TournamentService.MESSAGE_NO_MAX_ROUNDS, message);
    }

    @Test
    void ValidatesATournament_WithNoTimePerPlayer_ReturnsCorrespondingMessage() {
        // Arrange
        Tournament tournament = tournamentData.getTournaments().get(2);
        tournament.setTimePerPlayer(0);
        // Act
        String message = tournamentService.tournamentValidation(tournament);
        // Assert
        assertEquals(TournamentService.MESSAGE_NO_TIME_PER_PLAYER, message);
    }
}
