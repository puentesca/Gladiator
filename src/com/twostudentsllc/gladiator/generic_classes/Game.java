package com.twostudentsllc.gladiator.generic_classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.twostudentsllc.gladiator.managers.InventoryManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.twostudentsllc.gladiator.Main;
import com.twostudentsllc.gladiator.managers.GameLobby;
import com.twostudentsllc.gladiator.runnables.GameMapStartCountdown;

public abstract class Game {

    protected static String minigameName;
    protected static String minigameDisplayName;
    /**
     * A HashMap of all of the GameMaps and their names
     */
    protected HashMap<String, GameMap> maps;
    /**
     * Holds the manager for all of the queues for different maps
     */
    protected GameLobby mapQueues;

    /**
     * Manages the various kits for the game
     */
    protected InventoryManager kits;
    
    //TODO: Add calls to removePlayerFromQueue and stuff to allow players to instantly /gladiator join to another queue
    
    /**
     * Holds the countdowns for each map starting after enough players have joined a queue
     */
    protected HashMap<String, Countdown> mapStartCountdowns;

    protected Main plugin;
    
    public Game(Main plugin, String game, String displayName) {

        this.plugin = plugin;
    	minigameName = game;
        this.minigameDisplayName = displayName;

        maps = new HashMap<String, GameMap>();
        mapStartCountdowns = new HashMap<String, Countdown>();

        kits = new InventoryManager(plugin, game);
        mapQueues = new GameLobby(plugin, this);

        registerGame(this);
        loadAllMaps();
        //TODO: loadAllKits not necessary, automatically loads external file data in Inventory Manager
        //loadAllKits(); //TODO: Create serializable and deserialziable kits, as well as commands to save kits. Add commands and messages
        //for people to select kits before a game starts and assign a default kit
    }

    public HashMap<String, GameMap> getMaps() {
        return maps;
    }

    public Set<String> getMapNames() {
        return maps.keySet();
    }
    
    public InventoryManager getInventoryManager()
    {
    	return kits;
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
     * Starts a countdown until the map match starts after enough players have joined
     * @param mapName The name of the map
     */
    public void startMapCountdown(String mapName)
    {
    	//If the match is already starting
    	if(mapStartCountdowns.containsKey(mapName))
    		return;
    	System.out.println("Starting map countdown for map: " + mapName);
    	//TODO: Add ability to remove player from queue and stop countdown if someone disconnets and not enough players are left
    	Runnable r = new GameMapStartCountdown(plugin, this, mapName);
    	Countdown c = new Countdown(plugin, 15, r, true); //TODO: Add flexibility on the countdown for a match to start
    	mapStartCountdowns.put(mapName, c);
    	System.out.println("Started map countdown for map: " + mapName);
    }
    
    public Countdown getMapStartCountdown(String mapName)
    {
    	return mapStartCountdowns.get(mapName);
    }
    
    /**
     * Handles the time left in a map start countdown
     * @param mapName The name of the map that is starting soon
     * @param time The time left until starting
     */
    public void handleMapStartCountdown(String mapName, int time)
    {
    	Bukkit.broadcastMessage(minigameDisplayName + " match on map " + mapName + " is starting is " + time);
    	if(time <= 0)
    	{
    		startGame(mapName);
    		mapStartCountdowns.remove(mapName);
    	}
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
        return mapQueues.addGameQueue(mapName, targetMap.getMinTeams(), targetMap.getMaxTeams(), targetMap.getTeamSize());
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
     * Adds a player to a queue and inserts their data into the minigame stats table in the MySQL Database
     * @param mapName name of the map to add the player to
     * @param toAdd Player object to add
     * @return true/false whether it was successful
     */
    public boolean addPlayerToQueue(String mapName, Player toAdd) {
        if(!maps.containsKey(mapName))
        {
        	System.out.println("Game does not have map: " + mapName);
            return false;
        }
        plugin.getMysqlManager().getCommunicator().createMinigameStats(toAdd.getUniqueId(), minigameName);
        maps.get(mapName).sendPlayerToLobby(toAdd);
        return mapQueues.addPlayerToQueue(mapName, toAdd);
    }

    /**
     * Adds a group of players to a queue and inserts their data into the minigame stats table in the MySQL Database
     * @param mapName name of the map to add the group to
     * @param group ArrayList of Player objects
     * @return true/false whether it was successful
     */
    public boolean addGroupToQueue(String mapName, ArrayList<Player> group) {
        if(!maps.containsKey(mapName)) {
        	System.out.println("Game does not have map: " + mapName);
            return false;
        }
        for(Player p : group)
        {
        	plugin.getMysqlManager().getCommunicator().createMinigameStats(p.getUniqueId(), minigameName);
        	maps.get(mapName).sendPlayerToLobby(p);
        } 
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

//        if(!mapQueues.canMakeGame(mapName))
//            return false;

//        if(!targetMap.canStartMatch())
//            return false;

        //MapQueues getTeam method pulls players from queue (irreversible, can't pull the players out of queue until sure that a game can be started)
        ArrayList<Team> teams = mapQueues.getTeams(mapName);
        System.out.println("Game team size: " + teams.size());
        targetMap.startMatch(teams);
        return true;
    }

	/**
	 * Attempts to save all data relating to this game.
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
    
    public void registerMap(GameMap map)
    {
    	String mapName = map.getMapName();
    	maps.put(mapName, map);
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

    public InventoryManager getKits()
    {
    	return kits;
    }
    
}
