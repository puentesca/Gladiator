package com.twostudentsllc.gladiator.generic_classes;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.twostudentsllc.gladiator.Main;
import com.twostudentsllc.gladiator.global.DatabaseManager;

public abstract class Game {

    private String minigameName;
    private String minigameDisplayName;
    private HashMap<String, GameMap> maps;

    private Main plugin;
    
    public Game(Main plugin, String game, String displayName) {
        this.plugin = plugin;
    	minigameName = game;
        this.minigameDisplayName = displayName;
        maps = new HashMap<String, GameMap>();
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

    public GameMap getGameMap(String mapName) {
    	if(!maps.containsKey(mapName))
    		throw new IllegalArgumentException("The minigame '" + minigameName + "' does not have map '" + mapName + "'!");
        return maps.get(mapName);
    }

    public String getDisplayName() {
        return minigameDisplayName;
    }

    public String getGameName() {
        return minigameName;
    }

    //TODO: Add saveAllData() method which uses the DatabaseManager to save the hashmap of maps
    public boolean saveAllData()
    {
    	for(String s : maps.keySet())
    	{
    		GameMap map = maps.get(s);
    		map.saveLocations();
    		DatabaseManager.saveMap(map, minigameName, map.getMapName());
    	}
    	
    	
    	return true;
    }
    
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
