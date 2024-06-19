package com.valorant.backoffice.managers;

import com.valorant.models.Map;
import com.valorant.models.ModelFactory;
import com.valorant.repositories.MapRepository;

import java.io.BufferedReader;
import java.io.PrintStream;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.valorant.backoffice.utils.IOUtils.readLine;

public class MapsManager {
    private final PrintStream out;
    private final BufferedReader in;
    private final MapRepository mapRepository;
    private final ModelFactory modelFactory;

    public MapsManager(BufferedReader in, PrintStream out, MapRepository mapRepository, ModelFactory modelFactory) {
        this.out = out;
        this.in = in;
        this.mapRepository = mapRepository;
        this.modelFactory = modelFactory;
    }

    public void start() throws Exception {
        String command;
        do {
            showMenu();
            command = readLine(in);

            switch (command) {
                case "1" -> createMap();
                case "2" -> updateMap();
                case "3" -> deleteMap();
                case "4" -> viewMap();
                case "5" -> listMaps();
                case "back" -> out.println("Returning to main menu...");
                default -> out.println("Invalid command");
            }
        } while (!command.equalsIgnoreCase("back"));
    }

    private void showMenu() {
        out.println("1. Create Map");
        out.println("2. Update Map");
        out.println("3. Delete Map");
        out.println("4. View Map");
        out.println("5. List Maps");
        out.println("Type 'back' to return to the main menu");
    }

    private void createMap() {
        out.println("Creating a new Map");
        Map map = modelFactory.createMap();
        out.print("Enter Map Name: ");
        map.setName(readLine(in));
        out.print("Enter Map Type: ");
        map.setType(readLine(in));
        mapRepository.save(map);
        out.println("Map created successfully");
    }

    private void updateMap() {
        out.print("Enter Map ID to update: ");
        int id = Integer.parseInt(readLine(in));
        Map map = mapRepository.get(id);
        if (map != null) {
            out.print("Enter new Map Name (current: " + map.getName() + "): ");
            map.setName(readLine(in));
            out.print("Enter new Map Type (current: " + map.getType() + "): ");
            map.setType(readLine(in));
            mapRepository.save(map);
            out.println("Map updated successfully");
        } else {
            out.println("Map not found");
        }
    }

    private void deleteMap() {
        out.print("Enter Map ID to delete: ");
        int id = Integer.parseInt(readLine(in));
        Map map = mapRepository.get(id);
        if (map != null) {
            mapRepository.delete(map);
            out.println("Map deleted successfully");
        } else {
            out.println("Map not found");
        }
    }

    private void viewMap() {
        out.print("Enter Map ID to view: ");
        int id = Integer.parseInt(readLine(in));
        Map map = mapRepository.get(id);
        if (map != null) {
            out.println("Map ID: " + map.getId());
            out.println("Map Name: " + map.getName());
            out.println("Map Type: " + map.getType());
        } else {
            out.println("Map not found");
        }
    }

    private void listMaps() {
        out.println("Listing all maps");
        List<Map> sortedMaps = mapRepository.getAll().stream()
                .sorted(Comparator.comparingInt(Map::getId))
                .collect(Collectors.toList());
        for (Map map : sortedMaps) {
            out.println("Map ID: " + map.getId());
            out.println("Map Name: " + map.getName());
            out.println("Map Type: " + map.getType());
            out.println("----");
        }
    }
}
