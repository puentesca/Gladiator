package com.twostudentsllc.gladiator.generic_classes;

import org.bukkit.Server;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class Game {

    private String gameName;
    private String displayName;
    private HashMap<String, GameMap> maps;

    public Game(String gameName, String displayName) {
        this.gameName = gameName;
        this.displayName = displayName;
        maps = new HashMap<String, GameMap>();
    }

    public HashMap<String, GameMap> getMaps() {
        return maps;
    }

    public Set<String> getMapNames() {
        return maps.keySet();
    }
    
    public void addMap(String mapName, GameMap map)
    {
    	maps.put(mapName, map);
    }

    /**
    * Adds a given GameMap to the map list under its map name
    * @param args the arguments you wish to use to create the map
    */
    public abstract void createGameMap(String[] args);

    public GameMap getGameMap(String mapName) {
        return maps.get(mapName);
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getGameName() {
        return gameName;
    }

    /**
    * Gets a HashMap of all the maps that don't have active rounds
    * @return HashMap of GameMaps tied to their names
     */
    public HashMap<String, GameMap> getOpenMaps() {
        HashMap<String, GameMap> openMaps = new HashMap<>();

        for(Map.Entry<String, GameMap> mapEntry: getMaps().entrySet()) {
            if(!mapEntry.getValue().hasRunningRound) {
                openMaps.put(mapEntry.getKey(), mapEntry.getValue());
            }
        }

        return openMaps;
    }

    /**
    * Conditional check if there are any open maps
    * INFO: Overkill implementation for performance sake (method will probably be called a lot)
    * @return boolean if there are any open maps
     */
    public boolean hasOpenMaps() {
        HashMap<String, GameMap> openMaps = new HashMap<>();

        for(Map.Entry<String, GameMap> mapEntry: getMaps().entrySet()) {
            if(!mapEntry.getValue().hasRunningRound()) {
                return true;
            }
        }

        return false;
    }

}
