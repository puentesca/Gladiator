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
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

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
	 * Database directory location
	 */
	//private String databaseFileDirectory = "plugins/GladiatorData/";
	
	/**
	 * Database file name
	 */
	//private String databaseFileName = "locations.dat";
	
	/**
	 * Stores location data for plugin. Available locations are: bluespawn, redspawn, lobby
	 */
	//private HashMap<String, Location> locations;
	/**
	 * Stores possible keys for locations to be stored at
	 */
	private String[] possibleLocationKeys = {"redspawn", "bluespawn", "lobby", "spectating"};
	
	public LocationManager(Main plugin) throws FileNotFoundException, ClassNotFoundException, IOException
	{
		this.plugin = plugin;
		//locations = new HashMap<String, Location>();
		//loadLocationFile();
	}
	
	/**
	 * Saves all location data to a file by serializing each location into a string
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @param locationsToSave The locations in which you wish to save
	 * @param fileDir The directory in which you want to load the location data from
	 * @param fileName The name of the file in which you wish to load location data from
	 */
//	public void saveLocationFile(HashMap<String, Location> locationsToSave, String fileDir, String fileName) throws FileNotFoundException, IOException
//	{
//		File file = new File(fileDir + fileName);
//		
//		ObjectOutputStream output = new ObjectOutputStream(new GZIPOutputStream(new FileOutputStream(file)));
//		
//		HashMap<String, String> serializedLocations = new HashMap<String, String>();
//		//If the key exists in the locations hashmap, serialize it.
//		for(String s : possibleLocationKeys)
//		{
//			if(locationsToSave.containsKey(s))
//			{
//				String serializedL = Utils.serializeLocation(locationsToSave.get(s));
//				serializedLocations.put(s, serializedL);
//			}
//		}
//		
//		try {
//			output.writeObject(serializedLocations);
//			output.flush();
//			output.close();
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//		
//	}
	
	/**
	 * Loads the given location data into a hashmap from a file through deserialization.
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @param fileDir The directory in which you want to load the location data from
	 * @param fileName The name of the file in which you wish to load location data from
	 * @return The loaded location data
	 */
//	public HashMap<String, Location> loadLocationFile(String fileDir, String fileName) throws FileNotFoundException, IOException, ClassNotFoundException
//	{
//		File dir = new File(fileDir);
//		//If the directory doesnt exist, create it.
//		if(!dir.exists())
//		{
//			dir.mkdirs();
//		}
//		
//		File file = new File(fileDir + fileName);
//		//If the file doesnt exist
//		if(!file.exists()) {
//			try {
//			    file.createNewFile();
//			    return new HashMap<String, Location>();
//			} catch(IOException e) {
//			    e.printStackTrace();
//			}
//		}
//		
//		//Gets the files information
//		FileInputStream stream = new FileInputStream(file);
//		GZIPInputStream GZIP = new GZIPInputStream(stream);
//		ObjectInputStream input = new ObjectInputStream(GZIP);
//		Object readObject = input.readObject();
//		input.close();
//		
//		
//		//If the object read is not a hashmap
//		if(!(readObject instanceof HashMap))
//		{
//			throw new IOException("Object in " + fileName + " is not a HashMap.");
//		}
//			
//		HashMap<String, String> serializedLocations = (HashMap<String, String>) readObject;
//		HashMap<String, Location> convertedLocations = new HashMap<String, Location>();
//		//Updates HashMap to deserialized locations
//		for(String s : serializedLocations.keySet())
//		{
//			Location deserializedLocation = Utils.deserializeLocation(serializedLocations.get(s));
//			convertedLocations.put(s, deserializedLocation);
//			System.out.println("Location loaded: " + convertedLocations.get(s));
//		}
//			
//		return convertedLocations;	
//	}
	
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

		//TODO: Recreate a check to verify that the map key is valid. Check for formatting like spawn#_# where the first # is the team number starting at 0, and the second is the count of the current player
		//If the key is an invalid key
//		if(!isValidKey(sender, key))
//			return;
		
		//Gets the game from the game manager, then gets the GameMap from the game, and adds the location.
		//Will throw IllegalArgumentException if any of them do not exist
		plugin.getGameManager().getGame(minigameName).getGameMap(mapName).addLocation(key, location);
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
		
		//TODO: Recreate a check to verify that the map key is valid. Check for formatting like spawn#_# where the first # is the team number starting at 0, and the second is the count of the current player
				//If the key is an invalid key
		//If the key is an invalid key
//		if(!isValidKey(p, key))
//			return;
//		
		
		plugin.getGameManager().getGame(minigameName).getGameMap(mapName).getLocation(key);
		//TODO: 
		//Check if minigameName exists and gets the Game
		//Check if the mapName exists in the minigame and get the locations
		//Check if the key exists in the locations
		//Teleport the player to the location
//		if(!locations.containsKey(key))
//			return;
		//p.teleport(locations.get(key));
	}
	
	/**
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
	
	/**
	 * Creates a string representing the directory name of the database of a minigame
	 * @param minigameName The name of the directory the databases are linked to
	 * @return A string representing the file name
	 */
	public static String getDatabaseDirectoryString(String minigameName)
	{
		String dir = "/minigames/" + minigameName + "/";
		return dir;
	}
	
	/**
	 * Creates a string representing the file name of the locations of a game map
	 * @param mapName The name of the map the locations are linked to
	 * @return A string representing the file name
	 */
	public static String getDatabaseFileString(String mapName)
	{
		String file = mapName + "_locations.dat";
		return file;
	}
	
}
