package com.valorant.file.models;

import com.valorant.models.Match;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Player implements com.valorant.models.Player, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;  // Ensure consistent serialVersionUID

    private int id;
    private String username;
    private String displayName;
    private String email;
    private String region;
    private String rank;
    private Set<Match> matches = new HashSet<>();

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getRegion() {
        return region;
    }

    @Override
    public void setRegion(String region) {
        this.region = region;
    }

    @Override
    public String getRank() {
        return rank;
    }

    @Override
    public void setRank(String rank) {
        this.rank = rank;
    }

    @Override
    public Set<Match> getMatches() {
        return matches;
    }

    @Override
    public void setMatches(Set<Match> matches) {
        this.matches = matches;
    }
}
