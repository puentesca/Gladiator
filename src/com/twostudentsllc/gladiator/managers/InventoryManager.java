package com.twostudentsllc.gladiator.managers;

import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

import com.twostudentsllc.gladiator.global.DatabaseManager;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.twostudentsllc.gladiator.Main;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

public class InventoryManager {
	
	private HashMap<String, PlayerInventory> savedInventories;
	private String minigameName;
	
	private Main plugin;

	//Minigame name necessary as its used for file saving
	public InventoryManager(Main plugin, String minigameName)
	{
		HashMap<String, PlayerInventory> externalFile = DatabaseManager.loadInventories(minigameName);

		if(externalFile!=null)
			savedInventories = externalFile;

		this.plugin = plugin;
		savedInventories = new HashMap<String, PlayerInventory>();
		this.minigameName = minigameName;
	}

	/**
	 * Saves a player's inventory to the hashMap
	 * @param p player Object
	 * @param nameToSave String name to save the inventory under
	 */
	public void saveInventory(@NotNull Player p, @NotNull String nameToSave)
	{
		PlayerInventory inventory = p.getInventory();
		savedInventories.put(nameToSave, inventory);
		DatabaseManager.saveInventories(savedInventories, minigameName);
	}

	/**
	 * Helper method to force the database manager to save to a file
	 */
	public void forceSaveToFile() {
		DatabaseManager.saveInventories(savedInventories, minigameName);
	}

	/**
	 * Gets a player's inventory
	 * @param nameToSave Name to retrieve the player's inventory under
	 * @param forgetInventory whether to forget the inventory once it is pulled
	 * @return PlayerInventory object
	 */
	public PlayerInventory getInventory(@NotNull String nameToSave, boolean forgetInventory)
	{
		//Forget the inventory option
		if(forgetInventory)
			return savedInventories.remove(nameToSave);

		return savedInventories.get(nameToSave);

		//if the inventory should be removed from the database (if (forgetInventory)), then remove and return it using UUID.
		//inventoriesSaved.get(players UUID);
		//Remove this line and replace it so that it returns the
					 //players saved inventory from the inventoriesSaved
	}

	/**
	 * Returns a list of the possible inventories
	 * @return Set<String>
	 */
	public Set<String> listInventories() {
		return savedInventories.keySet();
	}

	/**
	 * Sets a player's inventory to one of the stored inventories
	 * @param toSet Player object whose inventory should be set
	 * @param targetInventoryName Name of the inventory the player should be set to
	 */
	public void setPlayerInventory(Player toSet, String targetInventoryName) {

		PlayerInventory playerInv = toSet.getInventory();
		PlayerInventory targetInv = savedInventories.get(targetInventoryName);

		if(targetInv==null)
			return;

		playerInv.setContents(targetInv.getContents());
	}
	
	
}
