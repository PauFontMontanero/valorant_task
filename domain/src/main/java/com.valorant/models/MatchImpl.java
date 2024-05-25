package com.valorant.models;

import java.time.LocalDateTime;

public class MatchImpl implements Match {
    private int id;
    private LocalDateTime playedOn;
    private int mapId;
    private String outcome;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public LocalDateTime getPlayedOn() {
        return playedOn;
    }

    @Override
    public void setPlayedOn(LocalDateTime playedOn) {
        this.playedOn = playedOn;
    }

    @Override
    public int getMapId() {
        return mapId;
    }

    @Override
    public void setMapId(int mapId) {
        this.mapId = mapId;
    }

    @Override
    public String getOutcome() {
        return outcome;
    }

    @Override
    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }
}
