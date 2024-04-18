package com.valorant.models;

import java.time.LocalDateTime;

// This interface represents a Match in the Valorant game.
public interface Match {
    // Get the unique identifier of the match.
    int getId();

    // Set the unique identifier of the match.
    void setId(int id);

    // Get the timestamp when the match was played.
    LocalDateTime getPlayedOn();

    // Set the timestamp when the match was played.
    void setPlayedOn(LocalDateTime playedOn);

    // Get the ID of the map associated with the match.
    int getMapId();

    // Set the ID of the map associated with the match.
    void setMapId(int mapId);

    // Get the outcome of the match (e.g., Victory, Defeat, Draw).
    String getOutcome();

    // Set the outcome of the match.
    void setOutcome(String outcome);
}
