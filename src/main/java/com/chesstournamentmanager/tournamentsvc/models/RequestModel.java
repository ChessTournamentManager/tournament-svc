package com.chesstournamentmanager.tournamentsvc.models;

import java.time.LocalDateTime;
import java.util.UUID;

public class RequestModel {
    private UUID hostId;
    private String name;
    private Tournament.Status status;
    private int rounds;
    private int timePerPlayer;

    public RequestModel(UUID hostId, String name, Tournament.Status status, int rounds, int timePerPlayer) {
        this.hostId = hostId;
        this.name = name;
        this.status = status;
        this.rounds = rounds;
        this.timePerPlayer = timePerPlayer;
    }

    public UUID getHostId() {
        return hostId;
    }

    public void setHostId(UUID hostId) {
        this.hostId = hostId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Tournament.Status getStatus() {
        return status;
    }

    public void setStatus(Tournament.Status status) {
        this.status = status;
    }

    public int getRounds() {
        return rounds;
    }

    public void setRounds(int rounds) {
        this.rounds = rounds;
    }

    public int getTimePerPlayer() {
        return timePerPlayer;
    }

    public void setTimePerPlayer(int timePerPlayer) {
        this.timePerPlayer = timePerPlayer;
    }
}
