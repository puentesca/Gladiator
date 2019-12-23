package com.twostudentsllc.gladiator.managers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import com.twostudentsllc.gladiator.generic_classes.Game;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.twostudentsllc.gladiator.Main;
import com.twostudentsllc.gladiator.generic_classes.GameMap;
import com.twostudentsllc.gladiator.global.Utils;

/**
 * A database that can store, save, load, and add any locations mapped to any key.
 * Copyright 2019 Casey Puentes. All rights reserved.
 * @author Casey Puentes
 *
 */

public class LocationManager {
	
	//TODO: Add reset location(s)
	
	private Main plugin;

	/**
	 * Stores possible keys for locations to be stored at
	 */
	private String[] possibleLocationKeys = {"spawn", "lobby", "spectate", "hub", "objective"};
	
	public LocationManager(Main plugin) throws FileNotFoundException, ClassNotFoundException, IOException
	{
		this.plugin = plugin;
	}
	
	/**
	 * Sets the new location of one of the available keys
	 * @param key The key of the new location
	 * @param l The location to save
	 */
	public void setLocation(Player sender, String[] args, Location location)
	{
		
		String minigameName = args[1];
		String mapName = args[2];
		String key = args[3];
		System.out.println("Setting location for '" + key + "' for map '" + mapName + "' and minigame '" + minigameName + "'.");

		//Handle input argument validation
		if(!validateArguments(sender, args))
			return;
		
		//Gets the game from the game manager, then gets the GameMap from the game, and adds the location.
		//Will throw IllegalArgumentException if any of them do not exist
		plugin.getGameManager().getGame(minigameName).getGameMap(mapName).addLocation(key, location);
		System.out.println("Location '" + key + "' for map '" + mapName + "' and minigame '" + minigameName + "' successfully set.");
		//TODO: Add confirmation that the map location was added. Make a confirmation method in Utils.
	}
	
	/**
	 * Teleports a player to a saved location
	 * @param p The player to be teleported
	 * @param key The key of the location to teleport to
	 */
	public void teleportToLocation(Player p, String[] args)
	{
		String minigameName = args[1];
		String mapName = args[2];
		String key = args[3];

		//Handle input argument validation
		if(!validateArguments(p, args))
			return;

		GameMap targetMap = plugin.getGameManager().getGame(minigameName).getGameMap(mapName);

		//Check if the location exists before trying to teleport the player
		Location targetLoc = targetMap.getLocation(key);
		if(targetLoc == null) {
			Utils.Error(p, key + " does not exist as a stored location for " + targetMap.getMapDisplayName());
			return;
		}
		
		p.teleport(plugin.getGameManager().getGame(minigameName).getGameMap(mapName).getLocation(key));
	}

	/*
	* Checks whether arguments passed in for locations are valid
	* Sends chat error message to sender if inputs invalid
	 */
	private boolean validateArguments(Player sender, String[] args) {

		String minigameName = args[1];
		String mapName = args[2];
		String key = args[3];

		System.out.println(minigameName + ":" + mapName + ":" + key);

		//Validate that the mini-game exists
		Game targetGame = plugin.getGameManager().getGame(minigameName);
		if(plugin.getGameManager().getGame(minigameName) == null) {
			Utils.Error(sender, "Minigame does not exist!");
			return false;
		}



		//Validate that the map exists
		GameMap targetMap = targetGame.getGameMap(mapName);
		if(targetGame.getGameMap(mapName) == null) {
			Utils.Error(sender, "Map does not exist!");
			return false;
		}

		//If the location is one of the valid location keys
		boolean invalidKey = true;
		for(String possibleKey: possibleLocationKeys) {
			if(!key.contains(possibleKey)) {
				invalidKey = false;
			}
		}

		if(invalidKey) {
			Utils.Error(sender, "Invalid Location");
			return false;
		}

		//Custom validation for whether the location is a spawn-point
		if(key.contains("spawn")) {
			Pattern spawnValidate = Pattern.compile("spawn\\d+_\\d+"); //Check if format is spawn#_#
			Matcher matcher = spawnValidate.matcher(key);
			boolean spawnValid = matcher.matches();

			if(!spawnValid) {
				Utils.Error(sender, "Spawn-point configuration invalid, the format is spawn#_#");
				return false;
			}
		}

		//Custom validation for whether the location is an objective
		if(key.contains("objective")) {
			Pattern spawnValidate = Pattern.compile("objective\\d+"); //Check if format is objective#
			Matcher matcher = spawnValidate.matcher(key);
			boolean spawnValid = matcher.matches();

			if(!spawnValid) {
				Utils.Error(sender, "Objective configuration invalid, the format is objective#");
				return false;
			}
		}

		return true;
	}
	
	/**
	 * FIXME: Remove? Might be unnecessary because of validateArguments
	 * Checks if the key is a valid key to be called upon
	 * @param key The key to be validated
	 * @return true if the key is valid
	 */
	private boolean isValidKey(Player sender, String key)
	{
		if(!Utils.arrayContains(possibleLocationKeys, key))
		{
			Utils.Error(sender, (Utils.chatMessage("Invalid location key of '" + key + "'! Available keys are: " + Arrays.toString(possibleLocationKeys), false)));
			return false;
		}
		return true;
	}
	
}
