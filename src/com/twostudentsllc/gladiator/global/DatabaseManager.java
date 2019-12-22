package com.twostudentsllc.gladiator.global;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.bukkit.Location;

import com.twostudentsllc.gladiator.generic_classes.GameMap;

/**
 * A global class that can save and load strings from locations
 * Copyright 2019 Casey Puentes. All rights reserved.
 * @author Casey Puentes
 *
 */
public class DatabaseManager {
	
	public static void saveLocations(HashMap<String, Location> locations, String minigameName, String mapName)
	{
		String fileDir = getDatabaseDirectoryString(minigameName);
		String fileName = getLocationFileNameString(mapName);
		
		File file = new File(fileDir + fileName);
		ObjectOutputStream output = null;
		try {
			output = new ObjectOutputStream(new GZIPOutputStream(new FileOutputStream(file)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//If it was unable to load the file
		if(output == null)
			return;
		
		try {
			output.writeObject(locations);
			output.flush();
			output.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	public static void saveMap(GameMap map, String minigameName, String mapName)
	{
		String fileDir = getDatabaseDirectoryString(minigameName);
		String fileName = getLocationFileNameString(mapName);
		
		File file = new File(fileDir + fileName);
		ObjectOutputStream output = null;
		try {
			output = new ObjectOutputStream(new GZIPOutputStream(new FileOutputStream(file)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//If it was unable to load the file
		if(output == null)
			return;
		
		try {
			output.writeObject(map);
			output.flush();
			output.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static HashMap<String, Location> loadLocations(String minigameName, String mapName)
	{
		String fileDir = getDatabaseDirectoryString(minigameName);
		String fileName = getLocationFileNameString(mapName);
		
		File dir = new File(fileDir);
		//If the directory doesnt exist, create it.
		if(!dir.exists())
		{
			dir.mkdirs();
		}
		
		File file = new File(fileDir + fileName);
		//If the file doesnt exist
		if(!file.exists()) {
			try {
			    file.createNewFile();
			    return new HashMap<String, Location>();
			} catch(IOException e) {
			    e.printStackTrace();
			}
		}
		
		Object readObject = null;
		//Gets the files information
		try {
			FileInputStream stream = new FileInputStream(file);
			GZIPInputStream GZIP = new GZIPInputStream(stream);
			ObjectInputStream input = new ObjectInputStream(GZIP);
			readObject = input.readObject();
			input.close();
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//If it didnt load
		if(readObject == null)
			return null;
		//If the object read is not a hashmap
//		if(!(readObject instanceof HashMap))
//		{
//			try {
//				throw new IOException("Object in " + fileName + " is not a HashMap.");
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			return null;
//		}
		
		return (HashMap<String, Location>) readObject;
		
	}
	
	public static GameMap loadMap(String minigameName, String mapName)
	{
		String fileDir = getDatabaseDirectoryString(minigameName);
		String fileName = getLocationFileNameString(mapName);
		
		File dir = new File(fileDir);
		//If the directory doesnt exist, create it.
		if(!dir.exists())
		{
			dir.mkdirs();
		}
		
		File file = new File(fileDir + fileName);
		//If the file doesnt exist
		if(!file.exists()) {
			throw new NullPointerException("Map does not exist!!");
		}
		
		Object readObject = null;
		//Gets the files information
		try {
			FileInputStream stream = new FileInputStream(file);
			GZIPInputStream GZIP = new GZIPInputStream(stream);
			ObjectInputStream input = new ObjectInputStream(GZIP);
			readObject = input.readObject();
			input.close();
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//If it didnt load
		if(readObject == null)
			return null;
		//If the object read is not a hashmap
//		if(!(readObject instanceof GameMap))
//		{
//			try {
//				throw new IOException("Object in " + fileName + " is not a HashMap.");
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			return null;
//		}
		
		return (GameMap) readObject;
		
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
	public static String getLocationFileNameString(String mapName)
	{
		String file = mapName + "_locations.dat";
		return file;
	}
}
