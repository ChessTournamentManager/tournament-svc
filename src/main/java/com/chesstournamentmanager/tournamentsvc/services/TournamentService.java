package com.chesstournamentmanager.tournamentsvc.services;

import com.chesstournamentmanager.tournamentsvc.models.Tournament;
import com.chesstournamentmanager.tournamentsvc.repositories.TournamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class TournamentService {

    private final TournamentRepository tournamentRepository;

    @Autowired
    public TournamentService(TournamentRepository tournamentRepository) {
        this.tournamentRepository = tournamentRepository;
    }

    // Read methods

    public Iterable<Tournament> getTournaments() {
        return tournamentRepository.findAll();
    }

    public Optional<Tournament> getTournament(UUID id) {
        return tournamentRepository.findById(id);
    }

    // Create methods

    public void addNewTournament(Tournament tournament) {
        tournamentRepository.save(tournament);
    }

    // Update methods

    @Transactional
    public void updateTournament(UUID id, UUID hostId, String name, Tournament.Status status, LocalDateTime startTime, int maxRounds, int timePerPlayer) {
        Tournament tournament = tournamentRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException(
                        "Tournament with id " + id + " does not exist"
                ));

        status = checkStatus(tournament, status, startTime);

        if (!tournament.getHostId().toString().isBlank() &&
            !Objects.equals(tournament.getHostId(), hostId)) {
            tournament.setHostId(hostId);
        }

        if (name.isBlank() &&
                !Objects.equals(tournament.getName(), name)) {
            tournament.setName(name);
        }

        if (status != null &&
                !Objects.equals(tournament.getStatus(), status)) {
            tournament.setStatus(status);
        }

        if (startTime != null &&
            !Objects.equals(tournament.getStartTime(), startTime)) {
            tournament.setStartTime(startTime);
        }

        if (maxRounds > 0 &&
                !Objects.equals(tournament.getMaxRounds(), maxRounds)) {
            tournament.setMaxRounds(maxRounds);
        }

        if (timePerPlayer > 0 &&
                !Objects.equals(tournament.getTimePerPlayer(), timePerPlayer)) {
            tournament.setTimePerPlayer(timePerPlayer);
        }
    }

    // Do something like this, but event-driven.
    @Transactional
    public void startTournament(Tournament tournament) {
        if (tournament.getStatus() == Tournament.Status.PLANNED && tournament.getStartTime().isBefore(LocalDateTime.now())) {
            tournament.setStatus(Tournament.Status.ONGOING);
        }
    }

    // Delete methods

    public void deleteTournament(UUID id) {
        boolean exists = tournamentRepository.existsById(id);
        if (!exists) {
            throw new IllegalStateException("Tournament with id " + id + " does not exist");
        }

        tournamentRepository.deleteById(id);
    }

    // Validation methods

    public String tournamentValidation(Tournament tournament) {
        if (!tournament.getHostId().toString().isBlank()) {
            return "No host ID was provided";
        }

        if (tournament.getName().isBlank()) {
            return "No name was provided";
        }

        if (tournament.getStatus() == null) {
            return "No status was provided";
        }

        if (plannedTournamentIsInTheFuture(tournament.getStatus(), tournament.getStartTime())) {
            return "A planned tournament may not start before the current time";
        }

        if (ongoingOrEndedTournamentIsInThePast(tournament.getStatus(), tournament.getStartTime())) {
            return "An tournament which is ongoing or has ended may not start after the current time";
        }

        if (tournament.getStartTime() == null){
            return "No start time was provided";
        }

        if (tournament.getMaxRounds() <= 0) {
            return "No maximum rounds were provided";
        }

        if (tournament.getTimePerPlayer() <= 0) {
            return "No amount of time per player was provided";
        }

        return "";
    }

    // Private methods

    private Tournament.Status checkStatus(Tournament tournament, Tournament.Status status, LocalDateTime startTime) {
        boolean statusChanged = false;
        Tournament.Status tempStatus;
        LocalDateTime tempStartTime;

        if (status != null && !Objects.equals(tournament.getStatus(), status)) {
            tempStatus = status;
            statusChanged = true;
        } else {
            tempStatus = tournament.getStatus();
        }

        if (startTime != null && !Objects.equals(tournament.getStartTime(), startTime)) {
            tempStartTime = startTime;
        } else {
            tempStartTime = tournament.getStartTime();
        }
        
        if (!plannedTournamentIsInTheFuture(tempStatus, tempStartTime)) {
            if (tempStartTime.isBefore(tournament.getFinishTime())) {
                return Tournament.Status.ONGOING;
            } else {
                return Tournament.Status.CANCELLED;
            }
        }
        
        if (!ongoingOrEndedTournamentIsInThePast(tempStatus, tempStartTime)) {
            if (statusChanged) {
                throw  new IllegalStateException("Can't put the tournament's status to this value due to its start date");
            } else {
                throw new IllegalStateException("Can't put the tournament's start date to this value due to its status");
            }
        }

        return status;
    }

//     Will always be true when checking a tournament that is not planned, because this method should only check planned tournaments.
//     Will be false when a tournament is planned, and it starts before the current time.
    private boolean plannedTournamentIsInTheFuture(Tournament.Status status, LocalDateTime startTime) {
        return status != Tournament.Status.PLANNED || startTime.isAfter(LocalDateTime.now());
    }

    // Will always be true when checking a tournament that is planned, because this method should only check tournaments that are ongoing or have ended.
    // Will be false when a tournament is ongoing or has ended, and it starts after the current time.
    private boolean ongoingOrEndedTournamentIsInThePast(Tournament.Status status, LocalDateTime startTime) {
        return status == Tournament.Status.PLANNED || startTime.isBefore(LocalDateTime.now());
    }
}
