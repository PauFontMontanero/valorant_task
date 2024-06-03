package com.valorant.models;

import java.io.Serializable;
import java.util.Set;

public interface Player extends Serializable {
    int getId();
    void setId(int id);

    String getUsername();
    void setUsername(String username);

    String getDisplayName();
    void setDisplayName(String displayName);

    String getEmail();
    void setEmail(String email);

    String getRegion();
    void setRegion(String region);

    String getRank();
    void setRank(String rank);

    Set<Match> getMatches();
    void setMatches(Set<Match> matches);
}
