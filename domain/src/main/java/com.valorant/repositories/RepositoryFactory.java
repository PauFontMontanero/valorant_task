package com.valorant.repositories;

public interface RepositoryFactory {
    AgentRepository getAgentRepository();
    MapRepository getMapRepository();
    MatchRepository getMatchRepository();
    PlayerRepository getPlayerRepository();
    WeaponRepository getWeaponRepository();
}
