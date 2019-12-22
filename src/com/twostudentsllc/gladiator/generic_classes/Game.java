package com.twostudentsllc.gladiator.generic_classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.twostudentsllc.gladiator.Main;
import com.twostudentsllc.gladiator.global.DatabaseManager;
import com.twostudentsllc.gladiator.managers.GameQueueManager;
import org.bukkit.entity.Player;

public abstract class Game {

    protected String minigameName;
    protected String minigameDisplayName;
    protected HashMap<String, GameMap> maps;
    protected GameQueueManager mapQueues;

    protected Main plugin;
    
    public Game(Main plugin, String game, String displayName) {
        this.plugin = plugin;
    	minigameName = game;
        this.minigameDisplayName = displayName;
        maps = new HashMap<String, GameMap>();
        loadAllMaps();
        mapQueues = new GameQueueManager(this);
    }

    public HashMap<String, GameMap> getMaps() {
        return maps;
    }

    public Set<String> getMapNames() {
        return maps.keySet();
    }

    /**
    * Adds a given GameMap to the map list under its map name
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
    		return null;
        return maps.get(mapName);
    }

    public String getDisplayName() {
        return minigameDisplayName;
    }

    public String getGameName() {
        return minigameName;
    }


    /**
     * Create a queue for a specific map
     * @param mapName name of the map
     * @return true/false whether the creation of queue was successful
     */
    public boolean createMapQueue(String mapName) {
        if(!maps.containsKey(mapName))
            return false;

        GameMap targetMap = maps.get(mapName);
        return mapQueues.addGameQueue(mapName, targetMap.minTeams, targetMap.maxTeams, targetMap.teamSize);
    }

    /**
     * Deletes a queue for a specific map
     * @param mapName name of the map
     * @return true/false whether the deletion of queue was successful
     */
    public boolean deleteGameQueue(String mapName) {
        return mapQueues.removeGameQueue(mapName);
    }

    /**
     * Adds a player to a queue
     * @param mapName name of the map to add the player to
     * @param toAdd Player object to add
     * @return true/false whether it was successful
     */
    public boolean addPlayerToQueue(String mapName, Player toAdd) {
        if(!maps.containsKey(mapName))
            return false;

        return mapQueues.addPlayerToQueue(mapName, toAdd);
    }

    /**
     * Adds a group of players to a queue
     * @param mapName name of the map to add the group to
     * @param group ArrayList of Player objects
     * @return true/false whether it was successful
     */
    public boolean addGroupToQueue(String mapName, ArrayList<Player> group) {
        if(!maps.containsKey(mapName))
            return false;

        return mapQueues.addGroupToQueue(mapName, group);
    }

    /**
     * Removes a player or group from a queue (works off of group leaders)
     * @param mapName name of map to remove group from
     * @param toRemove player to remove or team leader in a group
     * @return true/false whether it was successful
     */
    public boolean removePlayerFromQueue(String mapName, Player toRemove) {
        if(!maps.containsKey(mapName))
            return false;

        return mapQueues.removePlayerOrGroupFromQueue(mapName, toRemove);
    }


    /**
     * Check if there is an open map with enough players to start a new game
     * @return true/false whether there is a map with enough players and is not running a game
     */
    public boolean canStartGame() {

        HashMap<String, GameMap> openMaps = getOpenMaps();

        for(String mapName: openMaps.keySet()) {
            if(mapQueues.canMakeGame(mapName)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Check if you can start a game for a particular map
     * @param mapName name of map to check
     * @return true/false whether that map has a running game and has enough players in queue
     */
    public boolean canStartGameForMap(String mapName) {
        if(!maps.containsKey(mapName))
            return false;

        GameMap targetMap = maps.get(mapName);

        return !targetMap.hasRunningMatch && mapQueues.canMakeGame(mapName);
    }


    /**
     * Attempts to start a game for a particular map
     * Does checks before hand
     * @param mapName name of map to start a game for
     * @return true/false depending on whether game was successfully start (in-game issues are not dealt with here)
     */
    public boolean startGame(String mapName) {

        if(!maps.containsKey(mapName))
            return false;

        GameMap targetMap = maps.get(mapName);

        if(!mapQueues.canMakeGame(mapName))
            return false;

        if(!targetMap.canStartMatch())
            return false;

        //MapQueues getTeam method pulls players from queue (irreversible, can't pull the players out of queue until sure that a game can be started)
        targetMap.startMatch(mapQueues.getTeams(mapName));
        return true;
    }

    /**
     * Saves all map data
     * @return True if all data was saved successfully
     */
    public abstract boolean saveAllData();

    /**
     * Loads all maps associated with this Game
     */
    public abstract void loadAllMaps();
    
    /**
     * Registers the game with the GameManager
     * @param game The game to register
     */
    public void registerGame(Game game)
    {
    	plugin.getGameManager().registerGame(minigameName, game);
    }
    
    /**
    * Gets a HashMap of all the maps that don't have active rounds
    * @return HashMap of GameMaps tied to their names
     */
    public HashMap<String, GameMap> getOpenMaps() {
        HashMap<String, GameMap> openMaps = new HashMap<>();

        for(Map.Entry<String, GameMap> mapEntry: getMaps().entrySet()) {
            if(!mapEntry.getValue().hasRunningMatch) {
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
