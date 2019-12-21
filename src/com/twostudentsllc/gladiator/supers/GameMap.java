package com.twostudentsllc.gladiator.supers;

import java.io.IOException;
import java.util.HashMap;

import com.twostudentsllc.gladiator.managers.WorldManager;
import org.bukkit.Location;

import com.twostudentsllc.gladiator.Main;
import com.twostudentsllc.gladiator.arenas.MapRound;
import com.twostudentsllc.gladiator.managers.LocationManager;
import org.bukkit.World;

/**
 * Super class that stores all data relating to a maps special information
 * Copyright 2019 Casey Puentes. All rights reserved.
 * @author Casey Puentes
 *
 */
public abstract class GameMap {
	
	//TODO: Load maps from the Game's subclasses. Have abstract methods to load in the Game super class.
	//TODO: Create DatabaseManager to load and save strings in files from directories
	
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
	* The actual map instance
	 */
	protected World map;
	
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
	 * The minimum players the map can have to function
	 */
	protected int minPlayers;
	
	/**
	 * The maximum players the map can have to function
	 */
	protected int maxPlayers;
	
	/**
	 * Whether or not a round is currently running
	 */
	protected boolean hasRunningRound;
	
	/**
	 * The current round being played. Null if no round is being played
	 */
	protected MapRound currentRound;
	
	/**
	 * Creates a map with the data given. Must be called from a class extending the Game superclass
	 * @param plugin
	 * @param minigameName The minigame that this map is associated with.
	 * @param mapName The name of this map
	 * @param mapDisplayName The display name of this map
	 * @param minTeams The minimum amount of teams the map must have to function
	 * @param maxTeams The maximum amount of teams the map must have to function
	 * @param minPlayers The minimum players the map can have to function
	 * @param maxPlayers The maximum players the map can have to function
	 */
	public GameMap(Main plugin, String minigameName, String mapName, String mapDisplayName, int minTeams, int maxTeams, int minPlayers, int maxPlayers)
	{
		this.plugin = plugin;
		this.minigameName = minigameName;
		this.mapName = mapName;
		this.mapDisplayName = mapDisplayName;
		this.minTeams = minTeams;
		this.maxTeams = maxTeams;
		this.minPlayers = minPlayers;
		this.maxPlayers = maxPlayers;
		this.map = WorldManager.getWorld(plugin.getServer(), mapName);
		hasRunningRound = false;
		loadLocations();
	}

	//Method is called by Java when an object is going to be deleted
	@Override
	protected void finalize() {
		//Unloads loaded chunks in the world when the object is removed
		unloadChunks();
	}
	
	/**
	 * The locations for the map
	 */
	private HashMap<String, Location> locations;
	
	/**
	 * Loads all locations associated with this minigames map
	 */
	public void loadLocations()
	{
		LocationManager locMan = plugin.getLocationManager();
		//Pull locations from the location manager
		try {
			locations = locMan.loadLocationFile(LocationManager.getDatabaseDirectoryString(minigameName), LocationManager.getDatabaseFileString(mapName));
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
			System.out.println("ERROR: " + mapName + " locations did not load successfully!");
		}
		System.out.println("Successfully loaded map " + mapName + " locations for Minigame: " + minigameName + "");
	}
	
	/**
	 * Saves all locations associated with this map
	 */
	public void saveLocations()
	{
		LocationManager locMan = plugin.getLocationManager();
		//Pull locations from the location manager
		try {
			locMan.saveLocationFile(locations, LocationManager.getDatabaseDirectoryString(minigameName), LocationManager.getDatabaseFileString(mapName));
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("ERROR: " + mapName + " locations did not save successfully!");
		}
		System.out.println("Successfully saved map " + mapName + " locations for Minigame: " + minigameName + "");
	}


	/*
	* Unload loaded chunks on the map
	 */
	public void unloadChunks() {
		WorldManager.unloadLoadedChunks(plugin.getServer(), map);
	}
	
	/**
	 * Serializes the maps information
	 */
	public abstract String serialize();
	
	/**
	 * General method to start a round
	 */
	public abstract void startRound();
	
	/**
	 * General method to end a round
	 */
	public abstract void endRound();
	
	/**
	 * Gets whether or not a round can be started
	 * @return True if a round can be started
	 */
	public boolean canStartRound()
	{
		if(hasRunningRound())
		{
			System.out.println("Round is already runing!");
			return false;
		}
	
		//TODO: Add check to see if the Arraylist of teams passed in has a number of teams inside the minTeams and maxTeams bounds
		//Also check to see if there is a valid number of total players in the teams that is with the minPlayers and maxPlayers bounds
		//TODO: Check to see if there is one spawnpoint for every team.
		
		return true;
		
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
	
	/**
	 * @return the minimum players that the map can be played with
	 */
	public int getMinPlayers()
	{
		return minPlayers;
	}
	
	/**
	 * @return the maximum players that the map can be played with
	 */
	public int getMaxPlayers()
	{
		return maxPlayers;
	}

	/**
	 * @return The locations associated with this map
	 */
	public HashMap<String, Location> getLocations() {
		return locations;
	}
	
	/**
	 * Gets whether or not this map currently has a running round
	 * @returns True if a round is currently in progress on this map
	 */
	public boolean hasRunningRound()
	{
		return hasRunningRound;
	}
	
	
}
