package com.twostudentsllc.gladiator.global;

//import com.sun.org.apache.xpath.internal.res.XPATHErrorResources_zh_TW;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.Hash;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.*;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;


public class DatabaseManager {

	/**
	 * Saves a serializable object to a filepath
	 * @param object Any object that implements the serializable interface
	 * @param filePath the path where the object will be written to
	 */
	public static void saveSerializableObject(Serializable object, String filePath) {

		File file = new File(filePath);

		System.out.println("Actual file path: '" + file.getPath() + "'");

		//If the file doesnt exist
		if(!file.exists()) {
			try {
				file.getParentFile().mkdirs();
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
			output.writeObject(object);
			output.flush();
			output.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

	}

	/**
	 * Loads a serialized object from a file
	 * @param filePath where to load the object from
	 * @return Object the serialized object
	 */
	public static Object loadObjectFromFile(String filePath) {

		File file = new File(filePath);

		System.out.println("Actual file path: '" + file.getPath() + "'");
		//If the file doesnt exist
		if(!file.exists()) {
			try {
				file.getParentFile().mkdirs();
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

		return readObject;
	}

	/**
	 * Save custom inventories for a minigame
	 * @param inventoryList HashMap of custom inventories
	 * @param minigameName Name of the minigame
	 */
	public static void saveInventories(HashMap<String, Inventory> inventoryList, String minigameName) {

		String fileDir = getDatabaseDirectoryString(minigameName);
		String fileName = getDataFileNameString("inventories");

		HashMap<String, String> serializedMap = new HashMap<>();

		//Convert all inventories to base64 String
		for(Map.Entry<String, Inventory> inventory: inventoryList.entrySet()) {
			serializedMap.put(inventory.getKey(), Serializer.playerInventoryToBase64(inventory.getValue()));
		}

		System.out.println("Saving inventory data for minigame '" + minigameName  + "' at file: '" + fileDir + fileName + "'!");

		String filePath = fileDir + fileName;

		saveSerializableObject(serializedMap, filePath);
	}

	/**
	 * Loads custom inventories for a minigame
	 * @param minigameName Name of the minigame
	 * @return HashMap of inventories mapped by custom names to the inventories
	 */
	public static HashMap<String, Inventory> loadInventories(String minigameName) {
		String fileDir = getDatabaseDirectoryString(minigameName);
		String fileName = getDataFileNameString("inventories");

		System.out.println("Loading inventory data for minigame '" + minigameName  + "' at file: '" + fileDir + fileName + "'!");

		String filePath = fileDir + fileName;

		//Loads base64 serialized versions of inventory from file
		Object data = loadObjectFromFile(filePath);

		//If object does not exist
		if(data == null)
			return null;

		HashMap<String, String> serializedInventories = (HashMap<String, String>)data;

		try {

			//Converts inventories from base64 to Inventory object
			HashMap<String, Inventory> deserializedInventories = new HashMap<>();
			for (Map.Entry<String, String> base64Inventory : serializedInventories.entrySet())
				deserializedInventories.put(base64Inventory.getKey(), Serializer.playerInventoryFromBase64(base64Inventory.getValue()));

			return deserializedInventories;

		} catch(IllegalStateException e) {
			System.out.println("Could not load inventories!");
			return null;
		}
	}

	
	public static void saveLocations(HashMap<String, String> locations, String minigameName, String mapName)
	{
		String fileDir = getDatabaseDirectoryString(minigameName);
		String fileName = getLocationFileNameString(mapName);
		
		System.out.println("Saving location data for minigame '" + minigameName  + "' and map '" + mapName + "' at file: '" + fileDir + fileName + "'!");

		String filePath = fileDir + fileName;
		saveSerializableObject(locations, filePath);
/*
//		File dir = new File(fileDir);
//		//If the directory doesnt exist, create it.
//		if(!dir.exists())
//		{
//			dir.mkdirs();
//		}


		File file = new File(fileDir + fileName);

		System.out.println("Actual file path: '" + file.getPath() + "'");

		//If the file doesnt exist
		if(!file.exists()) {
			try {
				file.getParentFile().mkdirs();
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
		}*/

	}
	
	public static void saveMaps(HashMap<String, String> serializedMaps, String minigameName, String fileN)
	{
		String fileDir = getDatabaseDirectoryString(minigameName);
		String fileName = getDataFileNameString(fileN);
		
		System.out.println("Saving map data for minigame '" + minigameName  + "' at file: '" + fileDir + fileName + "'!");

		String filePath = fileDir + fileName;
		saveSerializableObject(serializedMaps, filePath);

	}
	
	public static HashMap<String, String> loadLocations(String minigameName, String mapName)
	{
		String fileDir = getDatabaseDirectoryString(minigameName);
		String fileName = getLocationFileNameString(mapName);
		
		System.out.println("Loading location data for minigame '" + minigameName  + "' and map '" + mapName + "' at file: '" + fileDir + fileName + "'!");

		String filePath = fileDir + fileName;

		//INFO: I don't think we should catch classcastexceptions since they are our fault
		return (HashMap<String, String>) loadObjectFromFile(filePath);

//		File dir = new File(fileDir);
//		//If the directory doesnt exist, create it.
//		if(!dir.exists())
//		{
//			dir.mkdirs();
//		}


		
		/*File file = new File(fileDir + fileName);
		
		System.out.println("Actual file path: '" + file.getPath() + "'");
		//If the file doesnt exist
		if(!file.exists()) {
			try {
				file.getParentFile().mkdirs();
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
		
		return (HashMap<String, String>) readObject;*/
		
	}
	
	//TODO: Make a check to stop from loading a duplicate world
	//TODO: Also make a check to stop location from loading duplicates. You can just do it by wiping the current files and replacing them with these.
	
	public static HashMap<String, String> loadMap(String minigameName, String fileN)
	{
		
		String fileDir = getDatabaseDirectoryString(minigameName);
		String fileName = getDataFileNameString(fileN);
		
		System.out.println("Loading map data for minigame '" + minigameName  + "' at file: '" + fileDir + fileName + "'!");

		String filePath = fileDir + fileName;

		return (HashMap<String, String>) loadObjectFromFile(filePath);
		
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
		String file = "" + fileName + ".dat";
		return file;
	}
}
