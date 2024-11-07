package com.valorant.services;

import com.valorant.domain.jdbc.repositories.JdbcRepositoryFactory;
import com.valorant.services.controllers.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class App {
    public static void main(String[] args) throws IOException {
        var repositoryFactory = new JdbcRepositoryFactory();
        var agentController = new AgentController(repositoryFactory.getAgentRepository());
        var mapController = new MapController(repositoryFactory.getMapRepository());
        var matchController = new MatchController(repositoryFactory.getMatchRepository());
        var playerController = new PlayerController(repositoryFactory.getPlayerRepository());
        var weaponController = new WeaponController(repositoryFactory.getWeaponRepository());

        Map<String, Controller> controllers = new HashMap<>();
        controllers.put("agent", agentController);
        controllers.put("map", mapController);
        controllers.put("match", matchController);
        controllers.put("player", playerController);
        controllers.put("weapon", weaponController);


        var requestRouter = new RequestRouterImpl(controllers);
        var server = new Server(requestRouter);
        server.start();
    }
}