package com.twostudentsllc.gladiator.managers.GameLobby;

import java.util.*;

import com.onarandombox.MultiverseCore.utils.WorldManager;
import com.twostudentsllc.gladiator.generic_classes.Countdown;
import com.twostudentsllc.gladiator.global.WorldUtils;
import com.twostudentsllc.gladiator.runnables.GameMapStartCountdown;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.twostudentsllc.gladiator.Main;
import com.twostudentsllc.gladiator.generic_classes.Game;
import com.twostudentsllc.gladiator.generic_classes.GameMap;
import com.twostudentsllc.gladiator.generic_classes.Team;

/**
 * Manages a queue of players for a Game
 */

//TODO: Handle putting players waiting for a game into the same world
//TODO: Add support for map votes
//TODO: Add support for kit selection
public class GameLobby {
	
	private Main plugin;
	//String key is the name of the map
	private Map<String, PlayerQueue> queueList;

	//Tracks countdowns for game match start for a map
	protected HashMap<String, Countdown> mapStartCountdowns;

	//Game the gamequeue is registered to
	private Game game;

	//The lobby world (world file is gamename_lobbyWorld)
	private World lobbyWorld;

	//TODO: Remove (Boolean used for testing)
	private static final boolean teleportToLobbyWorld = true;

    public GameLobby(Main plugin, Game game) {
    	this.plugin = plugin;
		this.game = game;
		this.lobbyWorld = Bukkit.getWorld(game.getGameName() + "_lobbyWorld");

		//Throw an error if the lobby world does not exist
		if(this.lobbyWorld == null) {

			WorldUtils.createVoidWorld(game.getGameName() + "_lobbyWorld");
			//throw new RuntimeException("There is no lobby world defined for " + game.getGameName());
		}

		queueList = new TreeMap<>();
    }


	/**
	 * Get the game the queues is registered to
	 * @return Game instance
	 */
	public Game getGame() {
		return game;
	}
    
    /**
     * Adds player to a game queue
     * @return boolean whether the player was successfully added
     */
    public boolean addPlayerToQueue(String mapName, Player toAdd) {

    	String key = mapName;

    	if(!queueList.containsKey(key)) {
    		System.out.println("No queue exists for the map: " + mapName);
    		return false;
    	}

    	boolean result = queueList.get(key).addPlayer(toAdd);
    	checkQueueStatus(mapName);

    	if(result)
    		System.out.println("Successfully added player to the queue for map: " + mapName);
    	else
    		System.out.println("Failed to add player to the queue for map: " + mapName);


		if(teleportToLobbyWorld)
			movePlayerIntoLobby(toAdd);

    	return result;
    }
    
    /**
     * Checks a queues status and sees if the queue has enough players to start. If so, it starts the countdown
     * @param mapName
     */
    public void checkQueueStatus(String mapName)
    {
    	if(canMakeGame(mapName)) {

    		//TODO: Implement voting logic here



    		startGameMapCountdown(mapName);
    	}
    	else
    	{
    		System.out.println("Not enough player to start a match for map: '" + mapName + "'. Current players: " + queueList.get(mapName).getTotalPlayersInQueue());
    	}
    	
    }

	/**
	 * Starts a countdown until the map match starts after enough players have joined
	 * @param mapName The name of the map
	 */
	public void startGameMapCountdown(String mapName)
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
		Bukkit.broadcastMessage(game.getDisplayName() + " match on map " + mapName + " is starting is " + time);
		//Additional check to insure that game can actually be started before the queue ends
		if(time <= 0 && canMakeGame(mapName))
		{
			game.startGame(mapName);
			mapStartCountdowns.remove(mapName);
		} else {
			System.out.println("Failed to start match for " + game.getDisplayName() + " Map: " + mapName);
		}
	}

	/**
	 * Adds a group of players to a game queue
	 * @return boolean whether the group was successfully added
	 */
    public boolean addGroupToQueue(String mapName, ArrayList<Player> group) {

		String key = mapName;

		if(!queueList.containsKey(key)) {
			System.out.println("No queue exists for the map: " + mapName);
			return false;
		}

		boolean result = queueList.get(key).addGroup(group);
    	checkQueueStatus(mapName);
    	if(result)
    		System.out.println("Successfully added player to the queue for map: " + mapName);
    	else
    		System.out.println("Unsuccessfully added player to the queue for map: " + mapName);

		if(teleportToLobbyWorld)
			movePlayersIntoLobby(group);
    	
    	return result;
	}
    
    /**
     * Removes player or group from game queue (same remove method since removal dependant on group leaders)
     * @return boolean whether the player was successfully removed
     */
    public boolean removePlayerOrGroupFromQueue(String mapName, Player toRemove) {

		String key = mapName;
    	
    	if(!queueList.containsKey(key)) {
    		return false;
    	}
    	
    	return queueList.get(key).removeGroupByPlayer(toRemove);
    }
    
    /**
     * Creates a game queue with some base requirement of players per game
	 * @param mapName map to add game queue for
	 * @param minimumTeams Minimum number of teams allowed on map
	 * @param maximumTeams Maximum number of teams allowed on map
	 * @param teamSize Size of each team
     * @return boolean whether the player was successfully removed
     */
    public boolean addGameQueue(String mapName, int minimumTeams, int maximumTeams, int teamSize) {

		String key = mapName;

    	if(queueList.containsKey(key)) {
    		return false;
    	}

    	if(!game.hasGameMap(mapName))
    		return false;
    	
    	
    	queueList.put(key, new PlayerQueue(game.getGameMap(mapName), minimumTeams, maximumTeams, teamSize));
    	System.out.println("Successfuly created a queue for map: " + mapName);
    	return true;
    }

	/**
	 * Remove a game queue for a map
	 * @param mapName name of the map to remove the game queue for
	 * @return true/false whether removing queue was successful
	 */
	public boolean removeGameQueue(String mapName) {

		String key = mapName;

    	if(!queueList.containsKey(key))
    		return false;

    	queueList.remove(key);
    	return true;
	}
    
    
    /**
     * Removes all disconnected players from queue
     */
    public void removeDisconnectedPlayers(String mapName) {

		String key =  mapName;

    	if(!queueList.containsKey(key))
    		return;
    	
    	queueList.get(key).removeDisconnectedPlayers();
    }
    
    /**
     * Checks if you have enough players to make a new game
     * @return boolean if there are enough players to make new game
     */
    public boolean canMakeGame(String mapName) {

		String key = mapName;

       	if(!queueList.containsKey(key)) {
       		System.out.println("QueueList does not contain a queue for: " + mapName);
    		return false;
    	}

       	//Removes any disconnected players
       	removeDisconnectedPlayers(mapName);
       	PlayerQueue targetGame = queueList.get(key);
       	
       	return targetGame.canMakeGame();
    }
    
    /**
     * Creates a list of players for a new game
     * @return null if there are not enough players or if the game queue does not exist,
     * 				otherwise it returns an ArrayList of Player instances
     */
    public ArrayList<Team> getTeams(String mapName) {

		String key = mapName;

       	if(!queueList.containsKey(key)) {
    		return null;
    	}
       	
       	if(canMakeGame(mapName)) {

	       	PlayerQueue targetGame = queueList.get(key);
	       	ArrayList<Player> players = targetGame.getPlayers();
	       	System.out.println("Grabbed " + players.size() + " players from the queue");

	       	//Split players into teams
			//TODO: This version splits people into teams randomly
			ArrayList<Team> teamList = new ArrayList<Team>();

			int q = 0;
			while(!players.isEmpty()) {

				Team newTeam = new Team(q);
				teamList.add(newTeam);

				//Fill a team with the required number of players and add them to their team
				for(int z = 0; z < targetGame.getTeamSize(); z++) {

					int i = (int) (Math.random() * players.size());
					Player toAdd = players.remove(i);

					newTeam.addPlayer(toAdd);
				}

				q++;
			}
			System.out.println("Generated " + (q + 1) + " teams");
			System.out.println("Size of teams arraylist: " + teamList.size());
			return teamList;

       	}
       	
       	return null;
    }

	/**
	 * Helper method that moves a player to the lobby world
	 * @param player
	 */
	private void movePlayerIntoLobby(Player player) {

		Location spawnPoint = lobbyWorld.getSpawnLocation();

		player.setGameMode(GameMode.ADVENTURE);
		player.setInvulnerable(true);
		player.teleport(spawnPoint);
	}

	private void movePlayersIntoLobby(ArrayList<Player> members) {
		Location spawnPoint = lobbyWorld.getSpawnLocation();

		//Teleport all players to lobby
		for(Player player: members) {
			player.teleport(spawnPoint);
			player.setGameMode(GameMode.ADVENTURE);
			player.setInvulnerable(true);
		}
	}

	public void sendPlayerBackToHub(Player player) {
		Location spawnPoint = plugin.getHubWorld().getSpawnLocation();

		player.setGameMode(GameMode.ADVENTURE);
		player.setInvulnerable(true);
		player.teleport(spawnPoint);
	}

	public void sendPlayersBackToHub(ArrayList<Player> members) {
		Location spawnPoint = plugin.getHubWorld().getSpawnLocation();

		//Teleport all players to lobby
		for(Player player: members) {
			player.teleport(spawnPoint);
			player.setGameMode(GameMode.ADVENTURE);
			player.setInvulnerable(true);
		}
	}

    
}