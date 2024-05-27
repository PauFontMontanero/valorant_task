-- Create the Valorant database if it doesn't exist
CREATE DATABASE IF NOT EXISTS VALORANT;

-- Switch to the Valorant database
USE VALORANT;

-- Table to store player information
CREATE TABLE PLAYER (
    PLAYER_ID INT PRIMARY KEY AUTO_INCREMENT, -- Unique identifier for the player
    USERNAME VARCHAR(50) NOT NULL, -- Player's username
    DISPLAY_NAME VARCHAR(50) NOT NULL, -- Player's display name
    EMAIL VARCHAR(100) NOT NULL, -- Player's email address
    REGION VARCHAR(50), -- Player's region
    `RANK` ENUM('Unranked', 'Iron 1', 'Iron 2', 'Iron 3', 'Bronze 1', 'Bronze 2', 'Bronze 3', 'Silver 1', 'Silver 2', 'Silver 3', 'Gold 1', 'Gold 2', 'Gold 3', 'Platinum 1', 'Platinum 2', 'Platinum 3', 'Diamond 1', 'Diamond 2', 'Diamond 3', 'Ascendant 1', 'Ascendant 2', 'Ascendant 3', 'Immortal 1', 'Immortal 2', 'Immortal 3', 'Radiant') -- Player's rank in Valorant
);

-- Table to store map information
CREATE TABLE MAP (
    MAP_ID INT PRIMARY KEY AUTO_INCREMENT, -- Unique identifier for the map
    NAME VARCHAR(100) NOT NULL, -- Name of the map
    TYPE ENUM('Competitive', 'Unranked', 'Spike Rush', 'Deathmatch', 'Team Deathmatch', 'Premier', 'Swift Play') NOT NULL -- Type of the map in Valorant
);

-- Table to store match information
CREATE TABLE `MATCH` (
    MATCH_ID INT PRIMARY KEY AUTO_INCREMENT, -- Unique identifier for the match
    PLAYED_ON DATETIME NOT NULL, -- Date and time when the match was played
    MAP_ID INT, -- Foreign key referencing the map played
    OUTCOME ENUM('Victory', 'Defeat', 'Draw') NOT NULL, -- Outcome of the match
    FOREIGN KEY (MAP_ID) REFERENCES MAP(MAP_ID) -- Relationship with the map table
);

-- Table to store agent information
CREATE TABLE AGENT (
    AGENT_ID INT PRIMARY KEY AUTO_INCREMENT, -- Unique identifier for the agent
    NAME VARCHAR(50) NOT NULL, -- Name of the agent
    DESCRIPTION VARCHAR(250) NOT NULL, -- Description of the agent
    ROLE ENUM('Duelist', 'Initiator', 'Controller', 'Sentinel') NOT NULL -- Role of the agent in Valorant
);

-- Table to store weapon information
CREATE TABLE WEAPON (
    WEAPON_ID INT PRIMARY KEY AUTO_INCREMENT, -- Unique identifier for the weapon
    NAME VARCHAR(100) NOT NULL, -- Name of the weapon
    TYPE ENUM('Sidearm', 'SMG', 'Rifle', 'Sniper Rifle', 'Shotgun', 'Machine Gun', 'Melee') NOT NULL -- Type of the weapon in Valorant
);

-- Table to associate players with matches
CREATE TABLE MATCH_PLAYER (
    MATCH_PLAYER_ID INT PRIMARY KEY AUTO_INCREMENT, -- Unique identifier for the match-player relationship
    MATCH_ID INT, -- Foreign key referencing the match
    PLAYER_ID INT, -- Foreign key referencing the player
    FOREIGN KEY (MATCH_ID) REFERENCES `MATCH`(MATCH_ID), -- Relationship with the match table
    FOREIGN KEY (PLAYER_ID) REFERENCES PLAYER(PLAYER_ID) -- Relationship with the player table
);

-- Table representing the one-to-one relationship between players and agents
CREATE TABLE PLAYER_AGENT (
    PLAYER_ID INT PRIMARY KEY, -- Foreign key referencing the player
    AGENT_ID INT, -- Foreign key referencing the agent
    FOREIGN KEY (PLAYER_ID) REFERENCES PLAYER(PLAYER_ID) ON DELETE CASCADE, -- Relationship with the player table
    FOREIGN KEY (AGENT_ID) REFERENCES AGENT(AGENT_ID) ON DELETE CASCADE, -- Relationship with the agent table
    UNIQUE (PLAYER_ID, AGENT_ID) -- Ensure uniqueness for ON DUPLICATE KEY UPDATE to work
);

-- Table representing the one-to-one relationship between players and weapons
CREATE TABLE PLAYER_WEAPON (
    PLAYER_ID INT PRIMARY KEY, -- Foreign key referencing the player
    WEAPON_ID INT, -- Foreign key referencing the weapon
    FOREIGN KEY (PLAYER_ID) REFERENCES PLAYER(PLAYER_ID) ON DELETE CASCADE, -- Relationship with the player table
    FOREIGN KEY (WEAPON_ID) REFERENCES WEAPON(WEAPON_ID) ON DELETE CASCADE -- Relationship with the weapon table
);
