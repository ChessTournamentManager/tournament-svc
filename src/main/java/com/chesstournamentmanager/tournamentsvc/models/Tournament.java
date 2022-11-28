package com.chesstournamentmanager.tournamentsvc.models;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Data
@RedisHash("Tournament")
public class Tournament  implements Serializable {

    @Serial
    private static final long serialVersionUID = 6828938851562689867L;

    public enum Status {
        PLANNED,
        ONGOING,
        FINISHED,
        ON_HOLD,
        CANCELLED
    }

    @Id
    private UUID id;
    private UUID hostId;
    private String name;
    private Status status;
    private LocalDateTime startTime;
    private int rounds;
    private int timePerPlayer;

    private LocalDateTime createdAt;


    public Tournament(UUID hostId, String name, Status status, LocalDateTime startTime, int rounds, int timePerPlayer) {
        this.id = UUID.randomUUID();
        this.hostId = hostId;
        this.name = name;
        this.status = status;
        this.startTime = startTime;
        this.rounds = rounds;
        this.timePerPlayer = timePerPlayer;
        this.createdAt = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public UUID getHostId() {
        return hostId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
