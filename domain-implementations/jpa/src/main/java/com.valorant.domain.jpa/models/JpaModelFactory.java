package com.valorant.domain.jpa.models;

import com.valorant.models.Agent;
import com.valorant.models.Map;

public class JpaModelFactory {

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
}
