package com.chesstournamentmanager.tournamentsvc.models;

import java.time.LocalDateTime;
import java.util.UUID;

public class RequestModel {
    private UUID hostId;
    private String name;
    private Tournament.Status status;
    private LocalDateTime startTime;
    private int maxRounds;
    private int timePerPlayer;

    public RequestModel(UUID hostId, String name, LocalDateTime startTime, int maxRounds, int timePerPlayer) {
        this.hostId = hostId;
        this.name = name;
        this.startTime = startTime;
        this.maxRounds = maxRounds;
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

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public int getMaxRounds() {
        return maxRounds;
    }

    public void setMaxRounds(int maxRounds) {
        this.maxRounds = maxRounds;
    }

    public int getTimePerPlayer() {
        return timePerPlayer;
    }

    public void setTimePerPlayer(int timePerPlayer) {
        this.timePerPlayer = timePerPlayer;
    }
}
