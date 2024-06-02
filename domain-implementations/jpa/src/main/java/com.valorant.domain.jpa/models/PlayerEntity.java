package com.valorant.domain.jpa.models;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "PLAYER")
public class PlayerEntity implements com.valorant.models.Player {

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

    @OneToOne
    @JoinColumn(name = "AGENT_ID", referencedColumnName = "AGENT_ID")
    private AgentEntity agent;

    @OneToOne
    @JoinColumn(name = "WEAPON_ID", referencedColumnName = "WEAPON_ID")
    private WeaponEntity weapon;

    @ManyToMany
    @JoinTable(
            name = "MATCH_PLAYER",
            joinColumns = @JoinColumn(name = "PLAYER_ID"),
            inverseJoinColumns = @JoinColumn(name = "MATCH_ID")
    )
    private Set<MatchEntity> matches;

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

    public Set<MatchEntity> getMatches() {
        return matches;
    }

    public void setMatches(Set<MatchEntity> matches) {
        this.matches = matches;
    }
}
