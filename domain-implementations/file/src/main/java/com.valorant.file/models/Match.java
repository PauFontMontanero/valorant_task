package com.valorant.file.models;

import java.io.Serializable;
import java.time.LocalDateTime;

// Represents a match in the Valorant game.
// Implements the Match interface and Serializable for object serialization.
public class Match implements com.valorant.models.Match, Serializable {
    private int id;                     // Unique identifier for the match.
    private LocalDateTime playedOn;    // Date and time when the match was played.
    private int mapId;                  // Identifier of the map associated with the match.
    private String outcome;             // Outcome of the match (e.g., victory or defeat).

    // Getter method for retrieving the unique identifier of the match.
    @Override
    public int getId() {
        return id;
    }

    // Setter method for setting the unique identifier of the match.
    @Override
    public void setId(int id) {
        this.id = id;
    }

    // Getter method for retrieving the date and time when the match was played.
    @Override
    public LocalDateTime getPlayedOn() {
        return playedOn;
    }

    // Setter method for setting the date and time when the match was played.
    @Override
    public void setPlayedOn(LocalDateTime playedOn) {
        this.playedOn = playedOn;
    }

    // Getter method for retrieving the identifier of the map associated with the match.
    @Override
    public int getMapId() {
        return mapId;
    }

    // Setter method for setting the identifier of the map associated with the match.
    @Override
    public void setMapId(int mapId) {
        this.mapId = mapId;
    }

    // Getter method for retrieving the outcome of the match.
    @Override
    public String getOutcome() {
        return outcome;
    }

    // Setter method for setting the outcome of the match.
    @Override
    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }
}
