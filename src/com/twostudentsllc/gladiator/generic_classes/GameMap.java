package com.twostudentsllc.gladiator.generic_classes;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.twostudentsllc.gladiator.Main;
import com.twostudentsllc.gladiator.global.DatabaseManager;
import com.twostudentsllc.gladiator.global.Utils;
import com.twostudentsllc.gladiator.managers.WorldManager;

/**
 * Super class that stores all data relating to a maps special information
 * Copyright 2019 Casey Puentes. All rights reserved.
 * @author Casey Puentes
 *
 */
public abstract class GameMap {
	
	//TODO: Load maps from the Game's subclasses. Have abstract methods to load in the Game super class.
	//TODO: Make game track the world the map is in
	
	/**
	 * The main plugin
	 */
	protected Main plugin;
	/**
	 * <p>The minigame that this map is associated with.</p>
	 * <p>This must exactly match the minigames defined name, as well as be lowercase.</p>
	 * Example: Gladiator would be 'gladiator'
	 */
	protected String minigameName;
	/**
	 * <p>The name of this map.</p>
	 * <p>This must be one word, as well as be lowercase.</p>
	 */
	protected String mapName;

	/*
	* The world the map is located in
	 */
	protected World mapWorld;
	
	/**
	 * <p>The display name of this map.</p>
	 * <p>This does not have to be one word, and is not case sensitive.</p>
	 */
	protected String mapDisplayName;
	
	/**
	 * The minimum amount of teams the map must have to function
	 */
	protected int minTeams;
	/**
	 * The maximum amount of teams the map must have to function
	 */
	protected int maxTeams;

	/**
	 * Number of players per team
	 */
	protected int teamSize;
	
	/**
	 * Whether or not a round is currently running
	 */
	protected boolean hasRunningMatch;
	
	/**
	 * The current round being played. Null if no round is being played
	 */
	protected MapMatch currentMatch;
	
	/**
	 * The locations for the map
	 */
	protected HashMap<String, Location> locations;
	
	/**
	 * The time that the warmup takes before starting a round
	 */
	protected int warmupTimeLimit;
	
	/**
	 * The time that the cooldown takes before starting a new round or ending the match
	 */
	protected int cooldownTimeLimit;
	/**
	 * Holds the total number of rounds that a match takes
	 */
	protected int totalRounds;
	
	
	/**
	 * Creates a map with the data given. Must be called from a class extending the Game superclass
	 * @param plugin
	 * @param minigameName The minigame that this map is associated with.
	 * @param mapName The name of this map
	 * @param mapDisplayName The display name of this map
	 * @param minTeams The minimum amount of teams the map must have to function
	 * @param maxTeams The maximum amount of teams the map must have to function
	 * @teamSize the size that each team should be
	 */
	public GameMap(Main plugin, String minigameName, String mapName, String mapDisplayName, int minTeams, int maxTeams, int teamSize, int warmupTime, int cooldownTime, int totalRounds)
	{
		this.plugin = plugin;
		this.minigameName = minigameName;
		this.mapName = mapName;
		this.mapDisplayName = mapDisplayName;
		this.minTeams = minTeams;
		this.maxTeams = maxTeams;
		this.teamSize = teamSize;
		this.warmupTimeLimit = warmupTime;
		this.cooldownTimeLimit = cooldownTime;
		this.totalRounds = totalRounds;
		this.mapWorld = WorldManager.getWorld(plugin.getServer(), mapName);
		hasRunningMatch = false;
		plugin.getGameManager().getGame(minigameName).registerMap(this);
		loadLocations();
		createQueue();
	}
	
	//Creates a queue for this map
	public void createQueue()
	{
		//Creates a queue for the map
		getGame().createMapQueue(mapName);
	}
	
	/**
	 * Loads all locations associated with this minigames map
	 */
	public void loadLocations()
	{
		HashMap<String, String> serializedLocations = DatabaseManager.loadLocations(minigameName, mapName);
		
		locations = new HashMap<String, Location>();
		for(String s : serializedLocations.keySet())
		{
			String locationKey = s;
			String[] parts = serializedLocations.get(locationKey).split(":");
			String worldName = parts[0];
			double x = Double.parseDouble(parts[1]);
			double y = Double.parseDouble(parts[2]);
			double z = Double.parseDouble(parts[3]);
			float yaw = Float.parseFloat(parts[4]);
			float pitch = Float.parseFloat(parts[5]);
			Location l = new Location(Bukkit.getServer().getWorld(worldName), x, y, z, yaw, pitch);
			locations.put(locationKey, l);
			
		}
		
		//If the locations dont exist
//		if(locations == null)
//		{
//			System.out.println("No saved locations found. Creating new hashmap.");
//			locations = new HashMap<String, Location>();
//		}
		System.out.println("Successfully loaded map " + mapName + " locations for Minigame: " + minigameName + "");
	}
	
	/**
	 * Saves all locations associated with this map
	 */
	public void saveLocations()
	{
		HashMap<String, String> serializedLocations = new HashMap<String, String>();
		
		for(String s : locations.keySet())
		{
			serializedLocations.put(s, Utils.serializeLocation(locations.get(s)));
		}
		
		DatabaseManager.saveLocations(serializedLocations, minigameName, mapName);
		System.out.println("Successfully saved map " + mapName + " locations for Minigame: " + minigameName + "");
	}
	
	/**
	 * Adds a location to the database. If it exists, it replaces it
	 * @param key The name of the location to be added
	 * @param location The position of the location to be added
	 */
	public void addLocation(String key, Location location)
	{
		//If the key already exists, replace it
		if(locations.containsKey(key))
		{
			locations.replace(key, location);
		}
		else
		{
			locations.put(key, location);
		}
	}
	
	/**
	 * Gets a specified location.
	 * @return location or null if it does not exist
	 */
	public Location getLocation(String locationKey)
	{
		if(!locations.containsKey(locationKey))
		{
			return null;
		}
		return locations.get(locationKey);
	}
	
	/*
	* Unload loaded chunks on the map
	 */
	public void unloadChunks() {
		WorldManager.unloadLoadedChunks(mapWorld);
	}
	
	/**
	 * Serializes the maps information
	 */
	public abstract String serialize();
	
	/**
	 * This function is in charge of starting a new match
	 */
	public abstract void startMatch(ArrayList<Team> teams);
	
	/**
	 * General method to end a match
	 */
	public abstract void endMatch();
	
	/**
	 * Gets whether or not a round can be started
	 * @return True if a round can be started
	 */
	public boolean canStartMatch()//ArrayList<Team> teams)
	{
		if(hasRunningRound())
		{
			System.out.println("Round is already runing!");
			return false;
		}
		//FIXME: These checks should be handled by queue in game
/*		//If the team amount is not within bounds set by the map
		if(teams.size() < minTeams || teams.size() > minTeams)
		{
			throw new IllegalArgumentException("Team amount now within map bounds!");
		}
		
		int players = 0;
		for(Team t : teams)
		{
			players += t.getTotalPlayers();
		}
		//If the player amount isnt within bounds set by the map
		if(players < minPlayers || players > maxPlayers)
		{
			throw new IllegalArgumentException("Player amount not within bounds set by map!");
		}*/
		return true;
		
	}

	public Game getGame()
	{
		return plugin.getGameManager().getGame(minigameName);
	}
	
	/**
	 * @return The name of the minigame this map is associated with
	 */
	public String getMinigameName() {
		return minigameName;
	}

	/**
	 * @return The restricted map name (One word, lowercase)
	 */
	public String getMapName() {
		return mapName;
	}

	/**
	 * @return The display name version of the map name
	 */
	public String getMapDisplayName() {
		return mapDisplayName;
	}

	/**
	 * @return the minimum teams required for a map to be playable
	 */
	public int getMinTeams() {
		return minTeams;
	}

	/**
	 * @return the maximum teams that the map can be played with
	 */
	public int getMaxTeams() {
		return maxTeams;
	}
	
	public int getTeamSize()
	{
		return teamSize;
	}
	
	
	public int getWarmupTimeLimit()
	{
		return warmupTimeLimit;
	}
	
	public int getCooldownTimeLimit()
	{
		return cooldownTimeLimit;
	}
	
	public int getTotalRounds()
	{
		
		return totalRounds;
	}

	/**
	 * @return The locations associated with this map
	 */
	public HashMap<String, Location> getLocations() {
		return locations;
	}
	
	/**
	 * @return This maps set spectating location
	 */
	public Location getSpectatingLocation()
	{
		return locations.get("spectate");
	}
	
	public Location getLobbyLocation()
	{
		return locations.get("lobby");
	}
	
	/**
	 * Teleports a player to the spectating location
	 * @param p The player to teleport
	 */
	public void sendPlayerToSpectate(Player p)
	{
		p.teleport(getSpectatingLocation());
	}
	/**
	 * Teleports a player to the lobby location and sets their gamemode to adventure
	 * @param p The player to teleport
	 */
	public void sendPlayerToLobby(Player p)
	{
		//TODO: Add the setting of a lobby kit or hub kit when the player is teleported back to the lobby.
		p.teleport(getLobbyLocation());
		p.setGameMode(GameMode.ADVENTURE);
	}
	
	/**
	 * Gets whether or not this map currently has a running round
	 * @returns True if a round is currently in progress on this map
	 */
	public boolean hasRunningRound()
	{
		return hasRunningMatch;
	}
	
	
}
