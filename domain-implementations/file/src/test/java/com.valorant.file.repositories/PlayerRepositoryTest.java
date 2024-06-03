package com.valorant.file.repositories;

import com.valorant.models.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PlayerRepositoryTest {
    private final String testDataPath = System.getProperty("user.dir") + "/src/test/resources/data/player-tests/test-players.ser";
    private final String originalDataPath = System.getProperty("user.dir") + "/src/test/resources/data/player-tests/players.ser";

    @BeforeEach
    void setUp() throws IOException {
        Path originalPath = Path.of(originalDataPath);
        if (!Files.exists(originalPath)) {
            Files.createDirectories(originalPath.getParent());
            Files.createFile(originalPath);
        }

        Path testPath = Path.of(testDataPath);
        if (!Files.exists(testPath)) {
            Files.createFile(testPath);
        }

        Files.copy(originalPath, testPath, StandardCopyOption.REPLACE_EXISTING);
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(Path.of(testDataPath));
    }

    @Test
    void saveNewPlayer() {
        var repository = new PlayerRepository(testDataPath);

        var player = createPlayer("ItzSebiii", "Puppeteer#EUW", "bladesilviu526@gmail.com", "EUW", "Immortal2");

        repository.save(player);

        assertTrue(player.getId() > 0);
        assertNotNull(repository.get(player.getId()).getUsername());

        repository.load();
        assertNotNull(repository.get(player.getId()));
        assertNotNull(repository.get(player.getId()).getUsername());
    }

    @Test
    void deletePlayer() throws IOException {
        var testDataPath = System.getProperty("user.dir") + "/src/test/resources/data/player-tests/test-delete-players.ser";
        var dataPath = System.getProperty("user.dir") + "/src/test/resources/data/player-tests/delete-players.ser";

        Files.copy(Path.of(testDataPath), Path.of(dataPath), StandardCopyOption.REPLACE_EXISTING);

        var repository = new PlayerRepository(dataPath);

        String username = "ItzSebiii";

        var playerToDelete = new com.valorant.file.models.Player();
        playerToDelete.setUsername(username);

        repository.save(playerToDelete);

        assertNotNull(repository.getByUsername(username));

        repository.delete(playerToDelete);

        assertNull(repository.getByUsername(username));
    }

    @Test
    void getPlayer() {
        String dataPath = System.getProperty("user.dir") + "/src/test/resources/data/player-tests/get-players.ser";

        var repository = new PlayerRepository(dataPath);

        var player = createPlayer("ItzSebiii", "Puppeteer#EUW", "bladesilviu526@gmail.com", "EUW", "Immortal2");

        repository.save(player);

        assertNotNull(repository.getByUsername("ItzSebiii"));
    }

    @Test
    void getAllPlayers() {
        String dataPath = System.getProperty("user.dir") + "/src/test/resources/data/player-tests/getAll-players.ser";

        var repository = new PlayerRepository(dataPath);

        repository.getAll().forEach(repository::delete);

        var player1 = createPlayer("player1", "Player One", "player1@example.com", "NA", "Platinum");
        var player2 = createPlayer("player2", "Player Two", "player2@example.com", "EU", "Gold");
        var player3 = createPlayer("player3", "Player Three", "player3@example.com", "APAC", "Diamond");

        repository.save(player1);
        repository.save(player2);
        repository.save(player3);

        assertEquals(3, repository.getAll().size());
    }

    @Test
    void getEmail() {
        String dataPath = System.getProperty("user.dir") + "/src/test/resources/data/player-tests/get-email.ser";

        var repository = new PlayerRepository(dataPath);

        var player = createPlayer("player1", "Player One", "player1@example.com", "NA", "Platinum");

        repository.save(player);

        assertEquals("player1@example.com", repository.get(player.getId()).getEmail());
    }

    @Test
    void getRank() {
        String dataPath = System.getProperty("user.dir") + "/src/test/resources/data/player-tests/get-rank.ser";

        var repository = new PlayerRepository(dataPath);

        var player = createPlayer("player2", "Player Two", "player2@example.com", "EUW", "Radiant");

        repository.save(player);

        assertEquals(player.getRank(), repository.get(player.getId()).getRank());
    }

    @Test
    void getByRegion() {
        String dataPath = System.getProperty("user.dir") + "/src/test/resources/data/player-tests/get-by-region.ser";

        var repository = new PlayerRepository(dataPath);

        var player1 = createPlayer("Max", "Maxito#fire", "maxdelacruz@gmail.com", "NA", "Gold");
        var player2 = createPlayer("Zoe", "ItzZoeee#2207", "zoe.balint@gmail.com", "EUW", "Silver");
        var player3 = createPlayer("Carles", "Carglass#Cambia", "carlgasscambia@gmail.com", "APAC", "Platinum");

        repository.save(player1);
        repository.save(player2);
        repository.save(player3);

        long expectedPlayersInEU = repository.getAll().stream()
                .filter(player -> player.getRegion().equals("EU"))
                .count();
        long expectedPlayersInNA = repository.getAll().stream()
                .filter(player -> player.getRegion().equals("NA"))
                .count();

        Set<Player> playersInEU = repository.getByRegion("EU");
        Set<Player> playersInNA = repository.getByRegion("NA");

        assertEquals(expectedPlayersInEU, playersInEU.size());
        assertEquals(expectedPlayersInNA, playersInNA.size());
    }

    @Test
    void getByDisplayName() {
        String dataPath = System.getProperty("user.dir") + "/src/test/resources/data/player-tests/get-by-displayname.ser";

        var repository = new PlayerRepository(dataPath);

        var player1 = createPlayer("Max", "Maxito#fire", "maxdelacruz@gmail.com", "NA", "Gold");
        var player2 = createPlayer("Zoe", "ItzZoeee#2207", "zoe.balint@gmail.com", "EUW", "Silver");
        var player3 = createPlayer("Carles", "Carglass#Cambia", "carlgasscambia@gmail.com", "APAC", "Platinum");

        repository.save(player1);
        repository.save(player2);
        repository.save(player3);

        long expectedPlayersWithNameMax = repository.getAll().stream()
                .filter(player -> player.getDisplayName().equals("Max"))
                .count();
        long expectedPlayersWithNameZoe = repository.getAll().stream()
                .filter(player -> player.getDisplayName().equals("Zoe"))
                .count();
        long expectedPlayersWithNameCarles = repository.getAll().stream()
                .filter(player -> player.getDisplayName().equals("Carles"))
                .count();

        Set<Player> playersWithNameMax = repository.getByDisplayName("Max");
        Set<Player> playersWithNameZoe = repository.getByDisplayName("Zoe");
        Set<Player> playersWithNameCarles = repository.getByDisplayName("Carles");

        assertEquals(expectedPlayersWithNameMax, playersWithNameMax.size());
        assertEquals(expectedPlayersWithNameZoe, playersWithNameZoe.size());
        assertEquals(expectedPlayersWithNameCarles, playersWithNameCarles.size());
    }

    private com.valorant.file.models.Player createPlayer(String username, String displayName, String email, String region, String rank) {
        var player = new com.valorant.file.models.Player();
        player.setUsername(username);
        player.setDisplayName(displayName);
        player.setEmail(email);
        player.setRegion(region);
        player.setRank(rank);
        return player;
    }
}
