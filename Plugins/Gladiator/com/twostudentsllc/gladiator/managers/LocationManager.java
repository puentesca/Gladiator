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
import com.twostudentsllc.gladiator.utils.Utils;

public class LocationManager {
	
	//TODO: Add reset location(s)
	
	private Main plugin;
	
	/**
	 * Database directory location
	 */
	private String databaseFileDirectory = "plugins/GladiatorData/";
	
	/**
	 * Database file name
	 */
	private String databaseFileName = "locations.dat";
	
	/**
	 * Stores location data for plugin. Available locations are: bluespawn, redspawn, lobby
	 */
	private HashMap<String, Location> locations;
	/**
	 * Stores possible keys for locations to be stored at
	 */
	private String[] possibleLocationKeys = {"redspawn", "bluespawn", "lobby", "spectating"};
	
	public LocationManager(Main plugin) throws FileNotFoundException, ClassNotFoundException, IOException
	{
		this.plugin = plugin;
		locations = new HashMap<String, Location>();
		loadLocationFile();
	}
	
	/**
	 * Saves all location data to a file by serializing each location into a string
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void saveLocationFile() throws FileNotFoundException, IOException
	{
		File file = new File(databaseFileDirectory + databaseFileName);
		
		ObjectOutputStream output = new ObjectOutputStream(new GZIPOutputStream(new FileOutputStream(file)));
		
		HashMap<String, String> serializedLocations = new HashMap<String, String>();
		//If the key exists in the locations hashmap, serialize it.
		for(String s : possibleLocationKeys)
		{
			if(locations.containsKey(s))
			{
				String serializedL = Utils.serializeLocation(locations.get(s));
				serializedLocations.put(s, serializedL);
			}
		}
		
		try {
			output.writeObject(serializedLocations);
			output.flush();
			output.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Loads all location data into a hashmap from a file through deserialization.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void loadLocationFile() throws FileNotFoundException, IOException, ClassNotFoundException
	{
		File dir = new File(databaseFileDirectory);
		//If the directory doesnt exist, create it.
		if(!dir.exists())
		{
			dir.mkdirs();
		}
		
		File file = new File(databaseFileDirectory + databaseFileName);
		//If the file doesnt exist
		if(!file.exists()) {
			try {
			    file.createNewFile();
			    return;
			} catch(IOException e) {
			    e.printStackTrace();
			}
		}
		
		
		if(file != null)
		{
			FileInputStream stream = new FileInputStream(file);
			GZIPInputStream GZIP = new GZIPInputStream(stream);
			
			ObjectInputStream input = new ObjectInputStream(GZIP);
			Object readObject = input.readObject();
			input.close();
			
			//If the object read is not a hashmap
			if(!(readObject instanceof HashMap))
			{
				throw new IOException("Object in " + databaseFileName + " is not a HashMap.");
			}
			
			HashMap<String, String> serializedLocations = (HashMap<String, String>) readObject;
			
			//Updates HashMap to deserialized locations
			for(String s : serializedLocations.keySet())
			{
				Location deserializedLocation = Utils.deserializeLocation(serializedLocations.get(s));
				locations.put(s, deserializedLocation);
				System.out.println("Location loaded: " + locations.get(s));
			}
			
			
			
			
		}
	}
	
	/**
	 * Sets the new location of one of the available keys
	 * @param key The key of the new location
	 * @param l The location to save
	 */
	public void setLocation(Player sender, String key, Location l)
	{
		//If the key is an invalid key
		if(!isValidKey(sender, key))
			return;
		//If the key is already in the hashmap, replace it.
		if(locations.containsKey(key))
		{
			locations.replace(key, l);
		}
		else
		{
			locations.put(key, l);
		}
	}
	
	/**
	 * Teleports a player to a saved location
	 * @param p The player to be teleported
	 * @param key The key of the location to teleport to
	 */
	public void teleportToLocation(Player p, String key)
	{
		//If the key is an invalid key
		if(!isValidKey(p, key))
			return;
		
		if(!locations.containsKey(key))
			return;
		
		p.teleport(locations.get(key));
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
			Utils.Error(sender, (Utils.chatMessage("Invalid location key! Available keys are: " + Arrays.toString(possibleLocationKeys), false)));
			return false;
		}
		return true;
	}
	
}
