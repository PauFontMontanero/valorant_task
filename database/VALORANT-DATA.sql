-- Use the VALORANT database
USE VALORANT;

-- Insert sample player data into the PLAYER table
INSERT INTO PLAYER (USERNAME, DISPLAY_NAME, EMAIL, REGION, `RANK`) VALUES
('ItzSebiii', 'ItzSebiii#2207', 'bladesilviu526@gmail.com', 'EUW', 'Immortal 2'), -- Sample player from Europe West with Immortal 2 rank
('Fontii99', 'Paquetemetes#EUW', 'paquetemetes@gmail.com', 'EUW', 'Silver 3'), -- Sample player from Europe West with Silver 3 rank
('TenZ', 'SEN TenZ#SEN', 'tysontenzvalorant@gmail.com', 'NA', 'Radiant'), -- Sample player from North America with Radiant rank
('Dymer123', 'Dymer123yt#123yt', 'dymer72@gmail.com', 'EUW', 'Unranked'), -- Sample player from Europe West with Unranked rank
('Asuna526', 'LOUD ASUNA#LOUD', 'asunaloudvalorant@gmail.com', 'APAC', 'Radiant'); -- Sample player from Asia-Pacific with Radiant rank

-- Insert map data into the MAP table
INSERT INTO MAP (NAME, TYPE) VALUES
('Ascent', 'Competitive'), -- Competitive map
('Bind', 'Swift Play'), -- Swift Play map
('Breeze', 'Spike Rush'), -- Spike Rush map
('IceBox', 'Competitive'), -- Competitive map
('Pearl', 'Premier'), -- Premier map
('Fracture', 'Competitive'); -- Competitive map

-- Insert match data into the MATCH table
INSERT INTO `MATCH` (PLAYED_ON, MAP_ID, OUTCOME) VALUES
('2024-02-29 10:00:00', 1, 'Victory'), -- Match played on Ascent resulting in Victory
('2024-03-01 11:30:00', 2, 'Defeat'), -- Match played on Bind resulting in Defeat
('2024-03-02 13:00:00', 3, 'Draw'); -- Match played on Breeze resulting in Draw

-- Insert agent data into the AGENT table
INSERT INTO AGENT (NAME, DESCRIPTION, ROLE) VALUES
('Jett', 'Representing her home country of South Korea, Jett''s agile and evasive fighting style lets her take risks no one else can. She runs circles around every skirmish, cutting enemies before they even know what hit them.', 'Duelist'), -- Duelist agent Jett.
('Brimstone', 'Joining from the U.S.A., Brimstone''s orbital arsenal ensures his squad always has the advantage. His ability to deliver utility precisely and safely make him the unmatched boots-on-the-ground commander.', 'Controller'), -- Controller agent Brimstone.
('Sova', 'Born from the eternal winter of Russia''s tundra, Sova tracks, finds, and eliminates enemies with ruthless efficiency and precision. His custom bow and incredible scouting abilities ensure that even if you run, you cannot hide.', 'Initiator'); -- Initiator agent Sova.

-- Insert weapon data into the WEAPON table
INSERT INTO WEAPON (NAME, TYPE) VALUES
('Phantom', 'Rifle'), -- Rifle weapon
('Vandal', 'Rifle'), -- Rifle weapon
('Operator', 'Sniper Rifle'); -- Sniper Rifle weapon

-- Insert player-match association data into the MATCH_PLAYER table
INSERT INTO MATCH_PLAYER (MATCH_ID, PLAYER_ID) VALUES
(1, 1), -- Player 1 participated in Match 1
(2, 2), -- Player 2 participated in Match 2
(3, 3), -- Player 3 participated in Match 3
(1, 2), -- Player 2 also participated in Match 1
(1, 3); -- Player 3 also participated in Match 1

-- Insert player-agent association data into the PLAYER_AGENT table
INSERT INTO PLAYER_AGENT (PLAYER_ID, AGENT_ID) VALUES
(1, 1), -- Player 1 is associated with Agent Jett
(2, 2), -- Player 2 is associated with Agent Brimstone
(3, 3); -- Player 3 is associated with Agent Sova

-- Insert player-weapon association data into the PLAYER_WEAPON table
INSERT INTO PLAYER_WEAPON (PLAYER_ID, WEAPON_ID) VALUES
(1, 1), -- Player 1 uses Phantom rifle
(2, 2), -- Player 2 uses Vandal rifle
(3, 3), -- Player 3 uses Operator sniper rifle
(1, 2), -- Player 1 also uses Vandal rifle
(1, 3); -- Player 1 also uses Operator sniper rifle