package com.valorant.backoffice.managers;

import com.valorant.models.Agent;
import com.valorant.models.ModelFactory;
import com.valorant.repositories.AgentRepository;

import java.io.BufferedReader;
import java.io.PrintStream;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.valorant.backoffice.utils.IOUtils.readLine;

public class AgentsManager {
    private final PrintStream out;
    private final BufferedReader in;
    private final AgentRepository agentRepository;
    private final ModelFactory modelFactory;

    public AgentsManager(BufferedReader in, PrintStream out, AgentRepository agentRepository, ModelFactory modelFactory) {
        this.out = out;
        this.in = in;
        this.agentRepository = agentRepository;
        this.modelFactory = modelFactory;
    }

    public void start() throws Exception {
        String command;
        do {
            showMenu();
            command = readLine(in);

            switch (command) {
                case "1" -> createAgent();
                case "2" -> updateAgent();
                case "3" -> deleteAgent();
                case "4" -> viewAgent();
                case "5" -> listAgents();
                case "back" -> out.println("Returning to main menu...");
                default -> out.println("Invalid command");
            }
        } while (!command.equalsIgnoreCase("back"));
    }

    private void showMenu() {
        out.println("1. Create Agent");
        out.println("2. Update Agent");
        out.println("3. Delete Agent");
        out.println("4. View Agent");
        out.println("5. List Agents");
        out.println("Type 'back' to return to the main menu");
    }

    private void createAgent() {
        out.println("Creating a new Agent");
        Agent agent = modelFactory.createAgent();
        out.print("Enter Agent Name: ");
        agent.setName(readLine(in));
        out.print("Enter Agent Description: ");
        agent.setDescription(readLine(in));
        out.print("Enter Agent Role: ");
        agent.setRole(readLine(in));
        agentRepository.save(agent);
        out.println("Agent created successfully");
    }

    private void updateAgent() {
        out.print("Enter Agent ID to update: ");
        int id = Integer.parseInt(readLine(in));
        Agent agent = agentRepository.get(id);
        if (agent != null) {
            out.print("Enter new Agent Name (current: " + agent.getName() + "): ");
            agent.setName(readLine(in));
            out.print("Enter new Agent Description (current: " + agent.getDescription() + "): ");
            agent.setDescription(readLine(in));
            out.print("Enter new Agent Role (current: " + agent.getRole() + "): ");
            agent.setRole(readLine(in));
            agentRepository.save(agent);
            out.println("Agent updated successfully");
        } else {
            out.println("Agent not found");
        }
    }

    private void deleteAgent() {
        out.print("Enter Agent ID to delete: ");
        int id = Integer.parseInt(readLine(in));
        Agent agent = agentRepository.get(id);
        if (agent != null) {
            agentRepository.delete(agent);
            out.println("Agent deleted successfully");
        } else {
            out.println("Agent not found");
        }
    }

    private void viewAgent() {
        out.print("Enter Agent ID to view: ");
        int id = Integer.parseInt(readLine(in));
        Agent agent = agentRepository.get(id);
        if (agent != null) {
            out.println("Agent ID: " + agent.getId());
            out.println("Agent Name: " + agent.getName());
            out.println("Agent Description: " + agent.getDescription());
            out.println("Agent Role: " + agent.getRole());
        } else {
            out.println("Agent not found");
        }
    }

    private void listAgents() {
        out.println("Listing all agents");
        List<Agent> sortedAgents = agentRepository.getAll().stream()
                .sorted(Comparator.comparingInt(Agent::getId))
                .collect(Collectors.toList());
        for (Agent agent : sortedAgents) {
            out.println("Agent ID: " + agent.getId());
            out.println("Agent Name: " + agent.getName());
            out.println("Agent Description: " + agent.getDescription());
            out.println("Agent Role: " + agent.getRole());
            out.println("----");
        }
    }
}
