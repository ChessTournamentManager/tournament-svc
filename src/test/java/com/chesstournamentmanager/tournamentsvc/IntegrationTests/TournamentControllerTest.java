package com.chesstournamentmanager.tournamentsvc.IntegrationTests;

import com.chesstournamentmanager.tournamentsvc.config.TournamentData;
import com.chesstournamentmanager.tournamentsvc.controllers.TournamentController;
import com.chesstournamentmanager.tournamentsvc.models.RequestModel;
import com.chesstournamentmanager.tournamentsvc.models.Tournament;
import com.chesstournamentmanager.tournamentsvc.services.TournamentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TournamentController.class)
public class TournamentControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TournamentService tournamentService;
    private AutoCloseable autoCloseable;
    private final Tournament tournament = TournamentData.getTournaments().get(0);
    private final ObjectMapper objectMapper = JsonMapper.builder()
            .findAndAddModules()
            .build();

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void GetsAllTournaments() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/tournament"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());

        verify(tournamentService).getTournaments();
    }

    @Test
    void GetsOneTournament() throws Exception {

        // Act & Assert
        when(tournamentService.getTournament(tournament.getId())).thenReturn(Optional.of(tournament));
        System.out.println(tournament.getStartTime().format(dateTimeFormatter));
        System.out.println(tournament.getFinishTime().format(dateTimeFormatter));
        System.out.println(tournament.getCreatedAt().format(dateTimeFormatter));
        mockMvc.perform(get("/tournament/" + tournament.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(tournament.getId().toString())))
                .andExpect(jsonPath("$.hostId", is(tournament.getHostId().toString())))
                .andExpect(jsonPath("$.name", is(tournament.getName())))
                .andExpect(jsonPath("$.status", is(tournament.getStatus().toString())))
                .andExpect(jsonPath("$.startTime", is(tournament.getStartTime().format(dateTimeFormatter))))
                .andExpect(jsonPath("$.finishTime", is(tournament.getFinishTime().format(dateTimeFormatter))))
                .andExpect(jsonPath("$.currentRound", is(tournament.getCurrentRound())))
                .andExpect(jsonPath("$.maxRounds", is(tournament.getMaxRounds())))
                .andExpect(jsonPath("$.timePerPlayer", is(tournament.getTimePerPlayer())))
                .andExpect(jsonPath("$.createdAt", is(tournament.getCreatedAt().format(dateTimeFormatter))))
                .andExpect(jsonPath("$").isNotEmpty());

        verify(tournamentService).getTournament(tournament.getId());
    }

    @Test
    void AddsANewTournament() throws Exception {

        // Arrange
        RequestModel requestModel = new RequestModel(
                tournament.getHostId(),
                tournament.getName(),
                tournament.getStartTime(),
                tournament.getMaxRounds(),
                tournament.getTimePerPlayer()
        );

        // Act & Assert
        when(tournamentService.tournamentValidation(isA(Tournament.class))).thenReturn("");
        when(tournamentService.getTournament(isA(UUID.class))).thenReturn(Optional.of(tournament));

        mockMvc.perform(post("/tournament/")
                    .content(objectMapper.writeValueAsString(requestModel))
                    .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(tournament.getId().toString())))
                .andExpect(jsonPath("$.hostId", is(tournament.getHostId().toString())))
                .andExpect(jsonPath("$.name", is(tournament.getName())))
                .andExpect(jsonPath("$.status", is(tournament.getStatus().toString())))
                .andExpect(jsonPath("$.startTime", is(tournament.getStartTime().format(dateTimeFormatter))))
                .andExpect(jsonPath("$.finishTime", is(tournament.getFinishTime().format(dateTimeFormatter))))
                .andExpect(jsonPath("$.currentRound", is(tournament.getCurrentRound())))
                .andExpect(jsonPath("$.maxRounds", is(tournament.getMaxRounds())))
                .andExpect(jsonPath("$.timePerPlayer", is(tournament.getTimePerPlayer())))
                .andExpect(jsonPath("$.createdAt", is(tournament.getCreatedAt().format(dateTimeFormatter))))
                .andExpect(jsonPath("$").isNotEmpty());

        verify(tournamentService).tournamentValidation(isA(Tournament.class));
        verify(tournamentService).addNewTournament(isA(Tournament.class));
        verify(tournamentService).getTournament(isA(UUID.class));
    }
}
