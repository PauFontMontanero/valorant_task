package com.valorant.backoffice.managers;

import com.valorant.models.ModelFactory;
import com.valorant.repositories.RepositoryFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;

import static com.valorant.backoffice.utils.IOUtils.readLine;

public class BackOffice {
    private final BufferedReader in;
    private final PrintStream out;
    private final RepositoryFactory repositoryFactory;
    private final ModelFactory modelFactory;

    public BackOffice(InputStream inputStream, OutputStream outputStream, RepositoryFactory repositoryFactory, ModelFactory modelFactory) {
        this.in = new BufferedReader(new InputStreamReader(inputStream));
        this.out = new PrintStream(outputStream);
        this.repositoryFactory = repositoryFactory;
        this.modelFactory = modelFactory;
    }

    public void start() throws Exception {
        showWelcomeMessage();

        String command;
        do {
            showMainMenu();
            command = readLine(in);

            switch (command) {
                case "1" -> manageAgents();
                case "2" -> manageMaps();
                case "3" -> manageMatches();
                case "4" -> managePlayers();
                case "5" -> manageWeapons();
                default -> {
                    if (!command.equalsIgnoreCase("exit")) {
                        out.println("Invalid command");
                    }
                }
            }
        } while (!command.equalsIgnoreCase("exit"));

        out.println("Bye!");
    }

    private void manageAgents() throws Exception {
        new AgentsManager(in, out, repositoryFactory.getAgentRepository(), modelFactory).start();
    }

    private void manageMaps() throws Exception {
        new MapsManager(in, out, repositoryFactory.getMapRepository(), modelFactory).start();
    }

    private void manageMatches() throws Exception {
        new MatchesManager(in, out, repositoryFactory.getMatchRepository(), modelFactory).start();
    }

    private void managePlayers() throws Exception {
        new PlayersManager(in, out, repositoryFactory.getPlayerRepository(), modelFactory).start();
    }

    private void manageWeapons() throws Exception {
        new WeaponsManager(in, out, repositoryFactory.getWeaponRepository(), modelFactory).start();
    }

    private void showWelcomeMessage() {
        out.println("Select a menu option or type exit to exit the application");
    }

    private void showMainMenu() {
        out.println("1. Manage Agents");
        out.println("2. Manage Maps");
        out.println("3. Manage Matches");
        out.println("4. Manage Players");
        out.println("5. Manage Weapons");
        out.println("Type 'exit' to exit the application");
    }
}
