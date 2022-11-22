package com.chesstournamentmanager.tournamentsvc.services;

import com.chesstournamentmanager.tournamentsvc.models.Tournament;
import com.chesstournamentmanager.tournamentsvc.repositories.TournamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class TournamentService {

    private final TournamentRepository tournamentRepository;

    @Autowired
    public TournamentService(TournamentRepository TournamentRepository) {
        this.tournamentRepository = TournamentRepository;
    }


    // GET

    public Iterable<Tournament> getTournaments() {
        return tournamentRepository.findAll();
    }

    public Optional<Tournament> getTournament(UUID id) {
        return tournamentRepository.findById(id);
    }


    // POST

    public void addNewTournament(Tournament tournament) {
        tournamentRepository.save(tournament);
    }


    // PUT


    @Transactional
    public void updateTournament(UUID id, UUID hostId, String name, Tournament.Status status, int rounds, int timePerPlayer) {
        Tournament tournament = tournamentRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException(
                        "Tournament with id " + id + " does not exist"
                ));

        if (name != null &&
                !name.isBlank() &&
                !Objects.equals(tournament.getName(), name)) {
            tournament.setName(name);
        }

        if (status != null &&
                !Objects.equals(tournament.getStatus(), status)) {
            tournament.setStatus(status);
        }

        if (rounds > 0 &&
                !Objects.equals(tournament.getRounds(), rounds)) {
            tournament.setRounds(rounds);
        }

        if (timePerPlayer > 0 &&
                !Objects.equals(tournament.getTimePerPlayer(), timePerPlayer)) {
            tournament.setTimePerPlayer(timePerPlayer);
        }
    }


    // DELETE

    public void deleteTournament(UUID id) {
        boolean exists = tournamentRepository.existsById(id);
        if (!exists) {
            throw new IllegalStateException("Tournament with id " + id + " does not exist");
        }

        tournamentRepository.deleteById(id);
    }


    public String TournamentValidation(Tournament tournament) {
        String message = "";

        return message;
    }
}
