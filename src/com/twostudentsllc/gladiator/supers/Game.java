package com.twostudentsllc.gladiator.supers;

import org.bukkit.Server;

import java.util.HashMap;
import java.util.Set;

public abstract class Game {

    private String gameName;
    private String displayName;
    private HashMap<String, GameMap> maps;

    public Game(String game, String displayName) {
        gameName = game;
        this.displayName = displayName;
        maps = new HashMap<String, GameMap>();
    }

    public HashMap<String, GameMap> getMaps() {
        return maps;
    }

    public Set<String> getMapNames() {
        return maps.keySet();
    }

    /*
    * Adds a given GameMap to the map list under its map name
    * @param mapName map's name
    * @param mapToAdd the actual GameMap instance to add
     */
    public void addGameMap(String mapName, GameMap mapToAdd) {
        if(maps.containsKey(mapName))
            return;
        maps.put(mapName, mapToAdd);
    }

    public GameMap getGameMap(String mapName) {
        return maps.get(mapName);
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getGameName() {
        return gameName;
    }
}
