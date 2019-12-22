package com.twostudentsllc.gladiator.generic_classes;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.twostudentsllc.gladiator.Main;
import com.twostudentsllc.gladiator.global.DatabaseManager;

public abstract class Game {

    protected String minigameName;
    protected String minigameDisplayName;
    protected HashMap<String, GameMap> maps;

    protected Main plugin;
    
    public Game(Main plugin, String game, String displayName) {
        this.plugin = plugin;
    	minigameName = game;
        this.minigameDisplayName = displayName;
        maps = new HashMap<String, GameMap>();
        loadAllMaps();
    }

    public HashMap<String, GameMap> getMaps() {
        return maps;
    }

    public Set<String> getMapNames() {
        return maps.keySet();
    }

    /**
    * Adds a given GameMap to the map list under its map name
    * 
    */
    public abstract void createGameMap(String[] args);
    
    /**
     * Gets whether or not the map exists
     * @param mapName The name of the map (Not display name)
     * @return True if the map exists
     */
    public boolean hasGameMap(String mapName)
    {
    	return maps.containsKey(mapName);
    }
    
    public GameMap getGameMap(String mapName) {
    	if(!hasGameMap(mapName))
    		throw new IllegalArgumentException("The minigame '" + minigameName + "' does not have map '" + mapName + "'!");
        return maps.get(mapName);
    }

    public String getDisplayName() {
        return minigameDisplayName;
    }

    public String getGameName() {
        return minigameName;
    }

    /**
     * Saves all map data
     * @return True if all data was saved successfully
     */
    public abstract boolean saveAllData();

    /**
     * Loads all map
     * s associated with this Game
     */
    public abstract void loadAllMaps();
    
    /**
     * Registers the game with the GameManager
     * @param game The game to register
     */
    public void registerGame(Game game)
    {
    	plugin.getGameManager().registerGame(minigameName, game, true);
    }
    
    /*
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

    /*
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
