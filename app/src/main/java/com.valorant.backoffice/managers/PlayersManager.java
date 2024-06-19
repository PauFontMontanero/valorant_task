package com.valorant.backoffice.managers;

import com.valorant.models.ModelFactory;
import com.valorant.models.Player;
import com.valorant.repositories.PlayerRepository;

import java.io.BufferedReader;
import java.io.PrintStream;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.valorant.backoffice.utils.IOUtils.readLine;

public class PlayersManager {
    private final PrintStream out;
    private final BufferedReader in;
    private final PlayerRepository playerRepository;
    private final ModelFactory modelFactory;

    public PlayersManager(BufferedReader in, PrintStream out, PlayerRepository playerRepository, ModelFactory modelFactory) {
        this.out = out;
        this.in = in;
        this.playerRepository = playerRepository;
        this.modelFactory = modelFactory;
    }

    public void start() throws Exception {
        String command;
        do {
            showMenu();
            command = readLine(in);

            switch (command) {
                case "1" -> createPlayer();
                case "2" -> updatePlayer();
                case "3" -> deletePlayer();
                case "4" -> viewPlayer();
                case "5" -> listPlayers();
                case "back" -> out.println("Returning to main menu...");
                default -> out.println("Invalid command");
            }
        } while (!command.equalsIgnoreCase("back"));
    }

    private void showMenu() {
        out.println("1. Create Player");
        out.println("2. Update Player");
        out.println("3. Delete Player");
        out.println("4. View Player");
        out.println("5. List Players");
        out.println("Type 'back' to return to the main menu");
    }

    private void createPlayer() {
        out.println("Creating a new Player");
        Player player = modelFactory.createPlayer();
        out.print("Enter Username: ");
        player.setUsername(readLine(in));
        out.print("Enter Display Name: ");
        player.setDisplayName(readLine(in));
        out.print("Enter Email: ");
        player.setEmail(readLine(in));
        out.print("Enter Region: ");
        player.setRegion(readLine(in));
        out.print("Enter Rank: ");
        player.setRank(readLine(in));
        playerRepository.save(player);
        out.println("Player created successfully");
    }

    private void updatePlayer() {
        out.print("Enter Player ID to update: ");
        int id = Integer.parseInt(readLine(in));
        Player player = playerRepository.get(id);
        if (player != null) {
            out.print("Enter new Username (current: " + player.getUsername() + "): ");
            player.setUsername(readLine(in));
            out.print("Enter new Display Name (current: " + player.getDisplayName() + "): ");
            player.setDisplayName(readLine(in));
            out.print("Enter new Email (current: " + player.getEmail() + "): ");
            player.setEmail(readLine(in));
            out.print("Enter new Region (current: " + player.getRegion() + "): ");
            player.setRegion(readLine(in));
            out.print("Enter new Rank (current: " + player.getRank() + "): ");
            player.setRank(readLine(in));
            playerRepository.save(player);
            out.println("Player updated successfully");
        } else {
            out.println("Player not found");
        }
    }

    private void deletePlayer() {
        out.print("Enter Player ID to delete: ");
        int id = Integer.parseInt(readLine(in));
        Player player = playerRepository.get(id);
        if (player != null) {
            playerRepository.delete(player);
            out.println("Player deleted successfully");
        } else {
            out.println("Player not found");
        }
    }

    private void viewPlayer() {
        out.print("Enter Player ID to view: ");
        int id = Integer.parseInt(readLine(in));
        Player player = playerRepository.get(id);
        if (player != null) {
            out.println("Player ID: " + player.getId());
            out.println("Username: " + player.getUsername());
            out.println("Display Name: " + player.getDisplayName());
            out.println("Email: " + player.getEmail());
            out.println("Region: " + player.getRegion());
            out.println("Rank: " + player.getRank());
        } else {
            out.println("Player not found");
        }
    }

    private void listPlayers() {
        out.println("Listing all players");
        List<Player> sortedPlayers = playerRepository.getAll().stream()
                .sorted(Comparator.comparingInt(Player::getId))
                .collect(Collectors.toList());
        for (Player player : sortedPlayers) {
            out.println("Player ID: " + player.getId());
            out.println("Username: " + player.getUsername());
            out.println("Display Name: " + player.getDisplayName());
            out.println("Email: " + player.getEmail());
            out.println("Region: " + player.getRegion());
            out.println("Rank: " + player.getRank());
            out.println("----");
        }
    }
}
