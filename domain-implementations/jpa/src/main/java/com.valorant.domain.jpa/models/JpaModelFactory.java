package com.valorant.domain.jpa.models;

import com.valorant.models.*;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public class JpaModelFactory implements ModelFactory {

    @Override
    public Agent createAgent() {
        return new AgentImpl();
    }

    @Override
    public Map createMap() {
        return new MapImpl();
    }

    @Override
    public Match createMatch() {
        return new MatchImpl();
    }

    @Override
    public Player createPlayer() {
        return new PlayerImpl();
    }

    @Override
    public Weapon createWeapon() {
        return new WeaponImpl();
    }

    public static AgentEntity toEntity(Agent agent) {
        AgentEntity entity = new AgentEntity();
        entity.setId(agent.getId());
        entity.setName(agent.getName());
        entity.setDescription(agent.getDescription());
        entity.setRole(agent.getRole());
        return entity;
    }

    public static Agent toModel(AgentEntity entity) {
        return new Agent() {
            @Override
            public int getId() {
                return entity.getId();
            }

            @Override
            public void setId(int id) {
                entity.setId(id);
            }

            @Override
            public String getName() {
                return entity.getName();
            }

            @Override
            public void setName(String name) {
                entity.setName(name);
            }

            @Override
            public String getDescription() {
                return entity.getDescription();
            }

            @Override
            public void setDescription(String description) {
                entity.setDescription(description);
            }

            @Override
            public String getRole() {
                return entity.getRole();
            }

            @Override
            public void setRole(String role) {
                entity.setRole(role);
            }
        };
    }

    public static MapEntity toEntity(Map map) {
        MapEntity entity = new MapEntity();
        entity.setId(map.getId());
        entity.setName(map.getName());
        entity.setType(map.getType());
        return entity;
    }

    public static Map toModel(MapEntity entity) {
        return new Map() {
            @Override
            public int getId() {
                return entity.getId();
            }

            @Override
            public void setId(int id) {
                entity.setId(id);
            }

            @Override
            public String getName() {
                return entity.getName();
            }

            @Override
            public void setName(String name) {
                entity.setName(name);
            }

            @Override
            public String getType() {
                return entity.getType();
            }

            @Override
            public void setType(String type) {
                entity.setType(type);
            }
        };
    }

    public static MatchEntity toEntity(Match match) {
        MatchEntity entity = new MatchEntity();
        entity.setId(match.getId());
        entity.setPlayedOn(match.getPlayedOn());
        entity.setOutcome(match.getOutcome());
        entity.setMapId(match.getMapId());
        return entity;
    }

    public static Match toModel(MatchEntity entity) {
        return new Match() {
            @Override
            public int getId() {
                return entity.getId();
            }

            @Override
            public void setId(int id) {
                entity.setId(id);
            }

            @Override
            public LocalDateTime getPlayedOn() {
                return entity.getPlayedOn();
            }

            @Override
            public void setPlayedOn(LocalDateTime playedOn) {
                entity.setPlayedOn(playedOn);
            }

            @Override
            public String getOutcome() {
                return entity.getOutcome();
            }

            @Override
            public void setOutcome(String outcome) {
                entity.setOutcome(outcome);
            }

            @Override
            public int getMapId() {
                return entity.getMap().getId();
            }

            @Override
            public void setMapId(int mapId) {
                if (entity.getMap() == null) {
                    entity.setMap(new MapEntity());
                }
                entity.getMap().setId(mapId);
            }
        };
    }

    public static WeaponEntity toEntity(Weapon weapon) {
        WeaponEntity entity = new WeaponEntity();
        entity.setId(weapon.getId());
        entity.setName(weapon.getName());
        entity.setType(weapon.getType());
        return entity;
    }

    public static Weapon toModel(WeaponEntity entity) {
        return new Weapon() {
            @Override
            public int getId() {
                return entity.getId();
            }

            @Override
            public void setId(int id) {
                entity.setId(id);
            }

            @Override
            public String getName() {
                return entity.getName();
            }

            @Override
            public void setName(String name) {
                entity.setName(name);
            }

            @Override
            public String getType() {
                return entity.getType();
            }

            @Override
            public void setType(String type) {
                entity.setType(type);
            }
        };
    }

    public static PlayerEntity toEntity(Player player) {
        PlayerEntity entity = new PlayerEntity();
        entity.setId(player.getId());
        entity.setUsername(player.getUsername());
        entity.setDisplayName(player.getDisplayName());
        entity.setEmail(player.getEmail());
        entity.setRegion(player.getRegion());
        entity.setRank(player.getRank());
        entity.setMatches(player.getMatches().stream().map(JpaModelFactory::toEntity).collect(Collectors.toSet()));
        return entity;
    }

    public static Player toModel(PlayerEntity entity) {
        Player player = new PlayerImpl();
        player.setId(entity.getId());
        player.setUsername(entity.getUsername());
        player.setDisplayName(entity.getDisplayName());
        player.setEmail(entity.getEmail());
        player.setRegion(entity.getRegion());
        player.setRank(entity.getRank());
        player.setMatches(entity.getMatches().stream().map(JpaModelFactory::toModel).collect(Collectors.toSet()));
        return player;
    }
}
