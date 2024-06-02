package com.valorant.domain.jpa.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "`MATCH`")
public class MatchEntity implements com.valorant.models.Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MATCH_ID")
    private int id;

    @Column(name = "PLAYED_ON", nullable = false)
    private LocalDateTime playedOn;

    @Column(name = "OUTCOME", nullable = false)
    private String outcome;

    @ManyToOne
    @JoinColumn(name = "MAP_ID")
    private MapEntity map;

    @ManyToMany(mappedBy = "matches")
    private Set<PlayerEntity> players;

    // Getters and setters
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
    public String getOutcome() {
        return outcome;
    }

    @Override
    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }

    public MapEntity getMap() {
        return map;
    }

    public void setMap(MapEntity map) {
        this.map = map;
    }

    @Override
    public int getMapId() {
        return map != null ? map.getId() : 0;
    }

    @Override
    public void setMapId(int mapId) {
        if (this.map == null) {
            this.map = new MapEntity();
        }
        this.map.setId(mapId);
    }

    public Set<PlayerEntity> getPlayers() {
        return players;
    }

    public void setPlayers(Set<PlayerEntity> players) {
        this.players = players;
    }
}
