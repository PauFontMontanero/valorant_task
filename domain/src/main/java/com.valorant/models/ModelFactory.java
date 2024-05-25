package com.valorant.models;

public interface ModelFactory {
    Agent createAgent();
    Map createMap();
    Match createMatch();
    Player createPlayer();
    Weapon createWeapon();
}
