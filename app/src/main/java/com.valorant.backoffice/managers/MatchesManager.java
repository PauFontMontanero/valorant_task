package com.valorant.backoffice.managers;

import com.valorant.models.Match;
import com.valorant.models.ModelFactory;
import com.valorant.repositories.MatchRepository;

import java.io.BufferedReader;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.valorant.backoffice.utils.IOUtils.readLine;

public class MatchesManager {
    private final PrintStream out;
    private final BufferedReader in;
    private final MatchRepository matchRepository;
    private final ModelFactory modelFactory;

    public MatchesManager(BufferedReader in, PrintStream out, MatchRepository matchRepository, ModelFactory modelFactory) {
        this.out = out;
        this.in = in;
        this.matchRepository = matchRepository;
        this.modelFactory = modelFactory;
    }

    public void start() throws Exception {
        String command;
        do {
            showMenu();
            command = readLine(in);

            switch (command) {
                case "1" -> createMatch();
                case "2" -> updateMatch();
                case "3" -> deleteMatch();
                case "4" -> viewMatch();
                case "5" -> listMatches();
                case "back" -> out.println("Returning to main menu...");
                default -> out.println("Invalid command");
            }
        } while (!command.equalsIgnoreCase("back"));
    }

    private void showMenu() {
        out.println("1. Create Match");
        out.println("2. Update Match");
        out.println("3. Delete Match");
        out.println("4. View Match");
        out.println("5. List Matches");
        out.println("Type 'back' to return to the main menu");
    }

    private void createMatch() {
        out.println("Creating a new Match");
        Match match = modelFactory.createMatch();
        out.print("Enter Match Played On (YYYY-MM-DDTHH:MM:SS): ");
        match.setPlayedOn(LocalDateTime.parse(readLine(in)));
        out.print("Enter Map ID: ");
        match.setMapId(Integer.parseInt(readLine(in)));
        out.print("Enter Match Outcome: ");
        match.setOutcome(readLine(in));
        matchRepository.save(match);
        out.println("Match created successfully");
    }

    private void updateMatch() {
        out.print("Enter Match ID to update: ");
        int id = Integer.parseInt(readLine(in));
        Match match = matchRepository.get(id);
        if (match != null) {
            out.print("Enter new Match Played On (current: " + match.getPlayedOn() + "): ");
            match.setPlayedOn(LocalDateTime.parse(readLine(in)));
            out.print("Enter new Map ID (current: " + match.getMapId() + "): ");
            match.setMapId(Integer.parseInt(readLine(in)));
            out.print("Enter new Match Outcome (current: " + match.getOutcome() + "): ");
            match.setOutcome(readLine(in));
            matchRepository.save(match);
            out.println("Match updated successfully");
        } else {
            out.println("Match not found");
        }
    }

    private void deleteMatch() {
        out.print("Enter Match ID to delete: ");
        int id = Integer.parseInt(readLine(in));
        Match match = matchRepository.get(id);
        if (match != null) {
            matchRepository.delete(match);
            out.println("Match deleted successfully");
        } else {
            out.println("Match not found");
        }
    }

    private void viewMatch() {
        out.print("Enter Match ID to view: ");
        int id = Integer.parseInt(readLine(in));
        Match match = matchRepository.get(id);
        if (match != null) {
            out.println("Match ID: " + match.getId());
            out.println("Played On: " + match.getPlayedOn());
            out.println("Map ID: " + match.getMapId());
            out.println("Outcome: " + match.getOutcome());
        } else {
            out.println("Match not found");
        }
    }

    private void listMatches() {
        out.println("Listing all matches");
        List<Match> sortedMatches = matchRepository.getAll().stream()
                .sorted(Comparator.comparingInt(Match::getId))
                .collect(Collectors.toList());
        for (Match match : sortedMatches) {
            out.println("Match ID: " + match.getId());
            out.println("Played On: " + match.getPlayedOn());
            out.println("Map ID: " + match.getMapId());
            out.println("Outcome: " + match.getOutcome());
            out.println("----");
        }
    }
}
