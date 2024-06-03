package com.valorant.domain.jpa.models;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "PLAYER")
public class PlayerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PLAYER_ID")
    private int id;

    @Column(name = "USERNAME", nullable = false)
    private String username;

    @Column(name = "DISPLAY_NAME", nullable = false)
    private String displayName;

    @Column(name = "EMAIL", nullable = false)
    private String email;

    @Column(name = "REGION")
    private String region;

    @Column(name = "`RANK`")
    private String rank;

    @ManyToMany
    @JoinTable(
            name = "MATCH_PLAYER",
            joinColumns = @JoinColumn(name = "PLAYER_ID"),
            inverseJoinColumns = @JoinColumn(name = "MATCH_ID")
    )
    private Set<MatchEntity> matches = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "AGENT_ID")
    private AgentEntity agent;

    @ManyToOne
    @JoinColumn(name = "WEAPON_ID")
    private WeaponEntity weapon;

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public Set<MatchEntity> getMatches() {
        return matches;
    }

    public void setMatches(Set<MatchEntity> matches) {
        this.matches = matches;
    }

    public AgentEntity getAgent() {
        return agent;
    }

    public void setAgent(AgentEntity agent) {
        this.agent = agent;
    }

    public WeaponEntity getWeapon() {
        return weapon;
    }

    public void setWeapon(WeaponEntity weapon) {
        this.weapon = weapon;
    }
}
