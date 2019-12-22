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
	
	public static void saveLocations(HashMap<String, String> locations, String minigameName, String mapName)
	{
		String fileDir = getDatabaseDirectoryString(minigameName);
		String fileName = getLocationFileNameString(mapName);
		
		System.out.println("Saving location data for minigame '" + minigameName  + "' and map '" + mapName + "' at file: '" + fileDir + fileName + "'!");
		
		File dir = new File(fileDir);
		//If the directory doesnt exist, create it.
		if(!dir.exists())
		{
			dir.mkdirs();
		}
		
		
		File file = new File(fileDir + fileName);
		
		System.out.println("Actual file path: '" + file.getPath() + "'");
		
		//If the file doesnt exist
		if(!file.exists()) {
			try {
				file.mkdirs();
			    file.createNewFile();
			} catch(IOException e) {
			    e.printStackTrace();
			}
		}
		
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
	
	public static void saveMaps(HashMap<String, String> serializedMaps, String minigameName, String fileN)
	{
		String fileDir = getDatabaseDirectoryString(minigameName);
		String fileName = getDataFileNameString(fileN);
		
		System.out.println("Saving map data for minigame '" + minigameName  + "' at file: '" + fileDir + fileName + "'!");
		
		File file = new File(fileDir + fileName);
		
		System.out.println("Actual file path: '" + file.getPath() + "'");
		
		File dir = new File(fileDir);
		//If the directory doesnt exist, create it.
		if(!dir.exists())
		{
			dir.mkdirs();
		}
		
		if(!file.exists()) {
			try {
				file.mkdirs();
			    file.createNewFile();
			} catch(IOException e) {
			    e.printStackTrace();
			}
		}
		
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
			output.writeObject(serializedMaps);
			output.flush();
			output.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static HashMap<String, String> loadLocations(String minigameName, String mapName)
	{
		String fileDir = getDatabaseDirectoryString(minigameName);
		String fileName = getLocationFileNameString(mapName);
		
		System.out.println("Loading location data for minigame '" + minigameName  + "' and map '" + mapName + "' at file: '" + fileDir + fileName + "'!");
		File dir = new File(fileDir);
		//If the directory doesnt exist, create it.
		if(!dir.exists())
		{
			dir.mkdirs();
		}
		
		File file = new File(fileDir + fileName);
		
		System.out.println("Actual file path: '" + file.getPath() + "'");
		//If the file doesnt exist
		if(!file.exists()) {
			try {
				//file.mkdirs();
			    file.createNewFile();
			    return new HashMap<String, String>();
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
		
		return (HashMap<String, String>) readObject;
		
	}
	
	//TODO: Make a check to stop from loading a duplicate world
	//TODO: Also make a check to stop location from loading duplicates. You can just do it by wiping the current files and replacing them with these.
	
	public static HashMap<String, String> loadMap(String minigameName, String fileN)
	{
		
		String fileDir = getDatabaseDirectoryString(minigameName);
		String fileName = getDataFileNameString(fileN);
		
		System.out.println("Loading map data for minigame '" + minigameName  + "' at file: '" + fileDir + fileName + "'!");
		
		File dir = new File(fileDir);
		//If the directory doesnt exist, create it.
		if(!dir.exists())
		{
			dir.mkdirs();
		}
		
		File file = new File(fileDir + fileName);
		
		System.out.println("Actual file path: '" + file.getPath() + "'");
		//If the file doesnt exist
		if(!file.exists()) {
			System.out.println("Maps HashMap does not exist! Creating an empty one.");
			try {
				//file.mkdirs();
			    file.createNewFile();
			    return new HashMap<String, String>();
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
		
		return (HashMap<String, String>) readObject;
		
	}
	
	
	/**
	 * Creates a string representing the directory name of the database of a minigame
	 * @param minigameName The name of the directory the databases are linked to
	 * @return A string representing the file name
	 */
	public static String getDatabaseDirectoryString(String minigameName)
	{
		String dir = "plugins/minigames/" + minigameName + "/";
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
	
	/**
	 * Creates a string representing the file name of any data file
	 * @param fileName The name you want the data file to have
	 * @return A string representing the file name
	 */
	public static String getDataFileNameString(String fileName)
	{
		String file = fileName + ".dat";
		return file;
	}
}
