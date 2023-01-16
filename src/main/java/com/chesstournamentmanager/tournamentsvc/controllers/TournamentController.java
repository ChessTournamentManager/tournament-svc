package com.chesstournamentmanager.tournamentsvc.controllers;

import com.chesstournamentmanager.tournamentsvc.models.RequestModel;
import com.chesstournamentmanager.tournamentsvc.models.Tournament;
import com.chesstournamentmanager.tournamentsvc.services.TournamentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(path = "tournament")
public class TournamentController {

    private final TournamentService tournamentService;

    @Autowired
    public TournamentController(TournamentService tournamentService) {
        this.tournamentService = tournamentService;
    }


    @GetMapping
    public ResponseEntity<List<Tournament>> getTournaments() {
        return new ResponseEntity<>(tournamentService.getTournaments(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tournament> getTournament(
            @PathVariable UUID id) {
        Optional<Tournament> tournament = tournamentService.getTournament(id);
        if (tournament.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Tournament with id " + id + " not found"
            );
        }
        return new ResponseEntity<>(tournament.get(), HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<Tournament> addTournament(@RequestBody RequestModel requestModel) {
        Tournament tournament = convertToEntity(requestModel);

        String message = tournamentService.tournamentValidation(tournament);
        if (!message.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    message
            );
        }

        tournamentService.addNewTournament(tournament);
        Optional<Tournament> returnedTournament = tournamentService.getTournament(tournament.getId());
        if (returnedTournament.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "The tournament was not added successfully. Ask the developers to fix this issue."
            );
        }
        return new ResponseEntity<>(returnedTournament.get(), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tournament> updateTournament(
            @PathVariable UUID id,
            @RequestParam(required = false) UUID hostId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Tournament.Status status,
            @RequestParam(required = false) LocalDateTime startTime,
            @RequestParam(required = false) int maxRounds,
            @RequestParam(required = false) int timePerPlayer) {
        tournamentService.updateTournament(id, hostId, name, status, startTime, maxRounds, timePerPlayer);
        Optional<Tournament> returnedTournament = tournamentService.getTournament(id);
        if (returnedTournament.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "The tournament was not updated successfully. Ask the developers to fix this issue."
            );
        }
        return new ResponseEntity<>(returnedTournament.get(), HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    public String deleteTournament(@PathVariable UUID id) {
        tournamentService.deleteTournament(id);
        return "Tournament with ID " + id + " has been deleted";
    }


    private Tournament convertToEntity(RequestModel requestModel) {
        return new Tournament(
                requestModel.getHostId(),
                requestModel.getName(),
                requestModel.getStatus(),
                requestModel.getStartTime(),
                requestModel.getMaxRounds(),
                requestModel.getTimePerPlayer()
        );
    }
}
