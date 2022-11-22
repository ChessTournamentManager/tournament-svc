package com.chesstournamentmanager.tournamentsvc.controllers;

import com.chesstournamentmanager.tournamentsvc.models.RequestModel;
import com.chesstournamentmanager.tournamentsvc.models.Tournament;
import com.chesstournamentmanager.tournamentsvc.services.TournamentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(path = "/tournament")
public class TournamentController {

    private final TournamentService tournamentService;
    private final ModelMapper modelMapper;

    @Autowired
    public TournamentController(TournamentService tournamentService, ModelMapper modelMapper) {
        this.tournamentService = tournamentService;
        this.modelMapper = modelMapper;
    }


    @GetMapping
    public Iterable<Tournament> getTournaments() {
        return tournamentService.getTournaments();
    }

    @GetMapping("/{id}")
    public Tournament getTournament(
            @PathVariable UUID id) {
        Optional<Tournament> tournament = tournamentService.getTournament(id);
        if (tournament.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Tournament with id " + id + " does not exist"
            );
        }
        return tournament.get();
    }


    @PostMapping
    public Tournament addUser(@RequestBody RequestModel requestModel) {
        Tournament tournament = convertToEntity(requestModel);

        String message = tournamentService.TournamentValidation(tournament);
        if (!message.isEmpty())
        {
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
        return returnedTournament.get();
    }

    @PutMapping("/{id}")
    public Tournament updateTournament(
            @PathVariable UUID id,
            @RequestParam(required = false) UUID hostId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Tournament.Status status,
            @RequestParam(required = false) int rounds,
            @RequestParam(required = false) int timePerPlayer) {
        tournamentService.updateTournament(id, hostId, name, status, rounds, timePerPlayer);
        Optional<Tournament> returnedUser= tournamentService.getTournament(id);
        if (returnedUser.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "The tournament was not updated successfully. Ask the developers to fix this issue."
            );
        }
        return returnedUser.get();
    }


    @DeleteMapping("/{id}")
    public String deleteTournament(@PathVariable UUID id) {
        tournamentService.deleteTournament(id);
        return "Tournament with ID " + id + " has been deleted";
    }


    private Tournament convertToEntity(RequestModel requestModel) {
        Tournament tournament = modelMapper.map(requestModel, Tournament.class);
        return tournament;
    }
}
