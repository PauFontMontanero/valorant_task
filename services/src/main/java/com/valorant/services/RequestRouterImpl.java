package com.valorant.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.valorant.models.*;
import com.valorant.services.controllers.Controller;
import com.valorant.services.exception.ResourceNotFoundException;
import com.valorant.services.exception.ServerErrorException;
import rawhttp.core.RawHttp;
import rawhttp.core.RawHttpRequest;
import rawhttp.core.RawHttpResponse;

import java.io.IOException;
import java.nio.charset.Charset;

public class RequestRouterImpl implements RequestRouter {

    private static final RawHttp rawHttp = new RawHttp();
    private final java.util.Map<String, Controller> controllers;

    public RequestRouterImpl(java.util.Map<String, Controller> controllers) {
        this.controllers = controllers;
    }

    public RawHttpResponse<?> execRequest(RawHttpRequest request) {
        var path = request.getUri().getPath();
        var method = request.getMethod();
        var pathParts = path.split("/");

        if (pathParts.length < 2) {
            throw new ResourceNotFoundException("Invalid request path");
        }

        var controllerName = pathParts[1];
        var responseJsonBody = "";
        RawHttpResponse<?> response;

        try {
            switch (controllerName) {
                case "agent":
                    responseJsonBody = manageAgent(request, method, pathParts);
                    break;
                case "map":
                    responseJsonBody = manageMap(request, method, pathParts);
                    break;
                case "match":
                    responseJsonBody = manageMatch(request, method, pathParts);
                    break;
                case "player":
                    responseJsonBody = managePlayer(request, method, pathParts);
                    break;
                case "weapon":
                    responseJsonBody = manageWeapon(request, method, pathParts);
                    break;
                default:
                    throw new ResourceNotFoundException("Controller not found");
            }

            response = rawHttp.parseResponse("HTTP/1.1 200 OK\r\n" +
                    "Content-Type: application/json\r\n" +
                    "Content-Length: " + responseJsonBody.length() + "\r\n" +
                    "\r\n" +
                    responseJsonBody);
        } catch (ResourceNotFoundException e) {
            response = rawHttp.parseResponse("HTTP/1.1 404 Not Found\r\n" +
                    "Content-Type: text/plain\r\n" +
                    "Content-Length: " + e.getMessage().length() + "\r\n" +
                    "\r\n" +
                    e.getMessage());
        } catch (Exception e) {
            response = rawHttp.parseResponse("HTTP/1.1 500 Internal Server Error\r\n" +
                    "Content-Type: text/plain\r\n" +
                    "Content-Length: " + e.getMessage().length() + "\r\n" +
                    "\r\n" +
                    e.getMessage());
        }

        return response;
    }

    // Manage requests related to Agent
    private String manageAgent(RawHttpRequest request, String method, String[] pathParts) {
        return manageRequest(request, method, pathParts, "agent", Agent.class, AgentImpl.class);
    }

    // Manage requests related to Map
    private String manageMap(RawHttpRequest request, String method, String[] pathParts) {
        return manageRequest(request, method, pathParts, "map", Map.class, MapImpl.class);
    }

    // Manage requests related to Match
    private String manageMatch(RawHttpRequest request, String method, String[] pathParts) {
        return manageRequest(request, method, pathParts, "match", Match.class, MatchImpl.class);
    }

    // Manage requests related to Player
    private String managePlayer(RawHttpRequest request, String method, String[] pathParts) {
        return manageRequest(request, method, pathParts, "player", Player.class, PlayerImpl.class);
    }

    // Manage requests related to Weapon
    private String manageWeapon(RawHttpRequest request, String method, String[] pathParts) {
        return manageRequest(request, method, pathParts, "weapon", Weapon.class, WeaponImpl.class);
    }

    // Generic method to manage the requests for each entity
    @SuppressWarnings("unchecked")
    private <T> String manageRequest(RawHttpRequest request, String method, String[] pathParts, String controllerName, Class<T> clazz, Class<? extends T> implClazz) {
        var controller = controllers.get(controllerName);
        var responseJsonBody = "";

        try {
            var mapper = new ObjectMapper();
            SimpleModule module = new SimpleModule();
            module.addAbstractTypeMapping(clazz, implClazz);
            mapper.registerModule(module);
            // Register JavaTimeModule to handle LocalDateTime

            if ("POST".equals(method)) {
                var json = request.getBody().get().decodeBodyToString(Charset.defaultCharset());
                var entity = mapper.readValue(json, clazz);
                controller.post(entity);

            } else if ("PUT".equals(method) && pathParts.length == 3) {
                var entityId = Integer.parseInt(pathParts[2]);
                var json = request.getBody().get().decodeBodyToString(Charset.defaultCharset());
                var entity = mapper.readValue(json, clazz);
                controller.put(entityId, entity);

            } else if ("DELETE".equals(method) && pathParts.length == 3) {
                var entityId = Integer.parseInt(pathParts[2]);
                controller.delete(entityId);

            } else if ("GET".equals(method) && pathParts.length == 3) {
                responseJsonBody = controller.get(Integer.parseInt(pathParts[2]));

            } else if ("GET".equals(method)) {
                responseJsonBody = controller.get();
            }
        } catch (JsonProcessingException e) {
            throw new ServerErrorException("Failed to process JSON", e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return responseJsonBody;
    }
}
