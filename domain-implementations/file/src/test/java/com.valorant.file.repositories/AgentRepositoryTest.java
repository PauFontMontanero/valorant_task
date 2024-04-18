// This class contains test cases for the AgentRepository class.
package com.valorant.file.repositories;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import static org.junit.jupiter.api.Assertions.*;

// Test cases for AgentRepository class
class AgentRepositoryTest {
    private final String testDataPath = System.getProperty("user.dir") + "/src/test/resources/data/agent-tests/test-agents.ser";
    private final String originalDataPath = System.getProperty("user.dir") + "/src/test/resources/data/agent-tests/agents.ser";

    // Executed before each test method
    @BeforeEach
    void setUp() throws IOException {
        // Log file paths for verification
        System.out.println("Original Data Path: " + originalDataPath);
        System.out.println("Test Data Path: " + testDataPath);

        // Check if the original data file exists; if not, create it
        Path originalPath = Path.of(originalDataPath);
        if (!Files.exists(originalPath)) {
            System.out.println("Original data file does not exist. Creating...");
            Files.createDirectories(originalPath.getParent());
            Files.createFile(originalPath);
        }

        // Check if the test data file exists; if not, create it
        Path testPath = Path.of(testDataPath);
        if (!Files.exists(testPath)) {
            System.out.println("Test data file does not exist. Creating...");
            Files.createFile(testPath);
        }

        // Copy the original data file to the test directory
        System.out.println("Copying original data file to test directory...");
        Files.copy(originalPath, testPath, StandardCopyOption.REPLACE_EXISTING);

        // Verify if the test data file exists after creation
        System.out.println("Test data file exists: " + Files.exists(testPath));
    }

    // Executed after each test method
    @AfterEach
    void tearDown() throws IOException {
        // Delete the test data file after each test
        System.out.println("Deleting test data file...");
        Files.deleteIfExists(Path.of(testDataPath));
    }

    // Test case for saving a new agent
    @Test
    void saveNewAgent() {
        // Create a new repository instance
        var repository = new AgentRepository(System.getProperty("user.dir") + "/src/main/resources/data/agents.ser");

        // Create a new agent
        var jett = createAgent("Jett", "Duelist", "Representing her home country of South Korea, Jett's agile and evasive fighting style lets her take risks no one else can. She runs circles around every skirmish, cutting enemies before they even know what hit them.");

        // Save the new agent
        repository.save(jett);

        // Assert that the agent was saved successfully
        assertTrue(jett.getId() > 0);
        assertNotNull(repository.get(jett.getId()).getRole());
        assertNotNull(repository.get(jett.getId()).getDescription()); // Asserting getDescription

        // Reload repository and verify saved agent
        repository.load();
        assertNotNull(repository.get(jett.getId()));
        assertNotNull(repository.get(jett.getId()).getRole());
        assertNotNull(repository.get(jett.getId()).getDescription()); // Asserting getDescription after reload
    }

    // Test case for updating an agent
    @Test
    void updateAgent() throws IOException {
        // Copy the update data file to the test directory
        var dataPath = System.getProperty("user.dir") + "/src/test/resources/data/agent-tests/update-agents.ser";
        var testDataPath = System.getProperty("user.dir") + "/src/test/resources/data/agent-tests/test-update-agents.ser";
        Files.copy(Path.of(dataPath), Path.of(testDataPath), StandardCopyOption.REPLACE_EXISTING);

        // Create a new repository instance with the test data
        var repository = new AgentRepository(testDataPath);

        // Create and save a new agent
        var reyna = createAgent("Reyna", "Duelist", "Forged in the heart of Mexico, Reyna dominates single combat, popping off with each kill she scores. Her capability is only limited by her raw skill, making her highly dependent on performance.");
        repository.save(reyna);

        // Retrieve the updated agent
        var updatedAgent = repository.get(1);

        // Assert that the agent was updated successfully
        assertEquals("Reyna", updatedAgent.getName());

        // Reload repository and verify updated agent
        repository.load();
        var updatedAgentFromFile = repository.get(1);
        assertEquals("Reyna", updatedAgentFromFile.getName());
    }

    // Test case for deleting an agent
    @Test
    void deleteAgent() throws IOException {
        // Define paths for temporary and test data files
        var testDataPath = System.getProperty("user.dir") + "/src/test/resources/data/agent-tests/test-delete-agents.ser";
        var dataPath = System.getProperty("user.dir") + "/src/test/resources/data/agent-tests/delete-agents.ser";

        // Copy the test data file to the temporary location
        Files.copy(Path.of(testDataPath), Path.of(dataPath), StandardCopyOption.REPLACE_EXISTING);

        // Create a repository instance with the temporary data file
        var repository = new AgentRepository(dataPath);

        // Define the ID of the agent to delete
        int id = 1;

        // Create a new agent instance
        var agentToDelete = new com.valorant.file.models.Agent();
        agentToDelete.setId(id);

        // Save the agent to the repository
        repository.save(agentToDelete);

        // Verify that the agent exists before deletion
        assertNotNull(repository.get(id));

        // Delete the agent
        repository.delete(agentToDelete);

        // Verify that the agent has been deleted
        assertNull(repository.get(id));
    }

    // Test case for retrieving an agent
    @Test
    void getAgent() {
        // Define the path to the data file
        String dataPath = System.getProperty("user.dir") + "/src/test/resources/data/agent-tests/get-agents.ser";

        // Create the repository using the data file path
        var repository = new AgentRepository(dataPath);

        // Create a new agent instance
        var agent = createAgent("Raze", "Duelist", "Raze explodes out of Brazil with her big personality and big guns. With her blunt-force-trauma playstyle, she excels at flushing entrenched enemies and clearing tight spaces with a generous dose of \"boom.\"");

        // Save the agent to the repository
        repository.save(agent);

        // Attempt to retrieve the agent from the repository and verify that it is not null
        assertNotNull(repository.get(agent.getId()));
    }

    // Test case for retrieving an agent by name
    @Test
    void getAgentByName() {
        // Define the path to the data file
        String dataPath = System.getProperty("user.dir") + "/src/test/resources/data/agent-tests/get-agents.ser";

        // Create the repository using the data file path
        var repository = new AgentRepository(dataPath);

        // Create a new agent instance
        var agent = createAgent("Harbor", "Controller", "Hailing from India’s coast, Harbor storms the field wielding ancient technology with dominion over water. He unleashes frothing rapids and crushing waves to shield his allies and pummel those that oppose him.");

        // Save the agent to the repository
        repository.save(agent);

        // Attempt to retrieve the agent by name from the repository and verify that it is not null
        assertNotNull(repository.getByName(agent.getName()));
    }

    // Test case for retrieving all agents
    @Test
    void getAllAgents() {
        // Define the path to the data file
        String dataPath = System.getProperty("user.dir") + "/src/test/resources/data/agent-tests/getAll-agents.ser";

        // Create the repository using the data file path
        var repository = new AgentRepository(dataPath);

        // Clear all agents from the repository
        repository.getAll().forEach(repository::delete);

        // Create multiple agent instances
        var agent1 = createAgent("Cypher", "Sentinel", "Cypher, the Moroccan sentinel, is a master of surveillance and control. He can gather intelligence, trap enemies, and secure objectives with ease.");
        var agent2 = createAgent("Brimstone", "Controller", "Brimstone, the American controller, brings the thunder with his arsenal of incendiary grenades, smokescreens, and orbital strikes.");
        var agent3 = createAgent("Killjoy", "Sentinel", "Killjoy, the German sentinel, is a genius inventor who deploys deadly gadgets to secure areas and control the battlefield.");

        // Save the agents to the repository
        repository.save(agent1);
        repository.save(agent2);
        repository.save(agent3);

        // Check if we can retrieve all agents from the repository
        assertEquals(3, repository.getAll().size());
    }

    // Helper method to create a new Agent instance
    private com.valorant.file.models.Agent createAgent(String name, String role, String description) {
        var agent = new com.valorant.file.models.Agent();
        agent.setName(name);
        agent.setRole(role);
        agent.setDescription(description);
        return agent;
    }
}
