-- Create the Valorant database if it doesn't exist
CREATE DATABASE IF NOT EXISTS VALORANT;

-- Switch to the Valorant database
USE VALORANT;

-- Create the table to store player information
CREATE TABLE PLAYER (

    PLAYER_ID INT PRIMARY KEY AUTO_INCREMENT,
    USERNAME VARCHAR(50) NOT NULL,
    DISPLAY_NAME VARCHAR(50) NOT NULL,
    EMAIL VARCHAR(100) NOT NULL,
    REGION VARCHAR(50),
    `RANK` ENUM('Unranked', 'Iron 1', 'Iron 2', 'Iron 3', 'Bronze 1', 'Bronze 2', 'Bronze 3', 'Silver 1', 'Silver 2', 'Silver 3', 'Gold 1', 'Gold 2', 'Gold 3', 'Platinum 1', 'Platinum 2', 'Platinum 3', 'Diamond 1', 'Diamond 2', 'Diamond 3', 'Ascendant 1', 'Ascendant 2', 'Ascendant 3', 'Immortal 1', 'Immortal 2', 'Immortal 3', 'Radiant')
);

-- Create the table to store map information
CREATE TABLE MAP (
    MAP_ID INT PRIMARY KEY AUTO_INCREMENT,
    NAME ENUM('Ascent', 'Bind', 'Breeze', 'IceBox', 'Lotus', 'Split', 'Sunset', 'Fracture', 'Haven', 'Pearl') NOT NULL,
    TYPE ENUM('Competitive', 'Unranked', 'Spike Rush', 'Deathmatch', 'Team Deathmatch', 'Premier', 'Swift Play') NOT NULL
);

CREATE TABLE `MATCH` (
    MATCH_ID INT PRIMARY KEY AUTO_INCREMENT,
    PLAYED_ON DATETIME NOT NULL,
    MAP_ID INT,
    OUTCOME ENUM('Victory', 'Defeat', 'Draw') NOT NULL,
    FOREIGN KEY (MAP_ID) REFERENCES MAP(MAP_ID)
);

-- Create the table to store agent information
CREATE TABLE AGENT (
    AGENT_ID INT PRIMARY KEY AUTO_INCREMENT,
    NAME VARCHAR(50) NOT NULL,
    DESCRIPTION VARCHAR(250) NOT NULL,
    ROLE ENUM('Duelist', 'Initiator', 'Controller', 'Sentinel') NOT NULL
);

-- Create the table to store weapon information
CREATE TABLE WEAPON (
    WEAPON_ID INT PRIMARY KEY AUTO_INCREMENT,
    NAME VARCHAR(100) NOT NULL,
    TYPE ENUM('Sidearm', 'SMG', 'Rifle', 'Sniper Rifle', 'Shotgun', 'Machine Gun', 'Melee') NOT NULL
);

-- Create the table to associate players with matches
CREATE TABLE MATCH_PLAYER (
    MATCH_PLAYER_ID INT PRIMARY KEY AUTO_INCREMENT,
    MATCH_ID INT,
    PLAYER_ID INT,
    FOREIGN KEY (MATCH_ID) REFERENCES `MATCH`(MATCH_ID),
    FOREIGN KEY (PLAYER_ID) REFERENCES PLAYER(PLAYER_ID)
);

-- Create the table to represent the one-to-one relationship between players and agents
CREATE TABLE PLAYER_AGENT (
    PLAYER_ID INT PRIMARY KEY,
    AGENT_ID INT,
    FOREIGN KEY (PLAYER_ID) REFERENCES PLAYER(PLAYER_ID) ON DELETE CASCADE,
    FOREIGN KEY (AGENT_ID) REFERENCES AGENT(AGENT_ID) ON DELETE CASCADE
);

-- Create the table to represent the one-to-one relationship between players and weapons
CREATE TABLE PLAYER_WEAPON (
    PLAYER_ID INT PRIMARY KEY,
    WEAPON_ID INT,
    FOREIGN KEY (PLAYER_ID) REFERENCES PLAYER(PLAYER_ID) ON DELETE CASCADE,
    FOREIGN KEY (WEAPON_ID) REFERENCES WEAPON(WEAPON_ID) ON DELETE CASCADE
);
