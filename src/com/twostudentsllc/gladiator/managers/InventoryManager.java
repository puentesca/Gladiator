package com.twostudentsllc.gladiator.managers;

import java.util.HashMap;
import java.util.Set;

import com.twostudentsllc.gladiator.global.DatabaseUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import com.twostudentsllc.gladiator.Main;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

/**
 * Manages kits an inventories for a minigame
 */
public class InventoryManager {
	
	private HashMap<String, Inventory> savedInventories;
	private String minigameName;
	
	private Main plugin;

	//Minigame name necessary as its used for file saving
	public InventoryManager(Main plugin, String minigameName)
	{
		HashMap<String, Inventory> externalFile = DatabaseUtils.loadInventories(minigameName);

		if(externalFile==null) {
			savedInventories = new HashMap<String, Inventory>();
		} else {
			savedInventories = externalFile;
			System.out.println("Loaded "+savedInventories.keySet()+" kits for " + minigameName);
		}

		this.plugin = plugin;
		this.minigameName = minigameName;
	}

	/**
	 * Converts a player inventory into super type Inventory, necessary since playerInventory does not contain armor
	 * @param other A inventory object that should be copied
	 * @return Deep copy of inventory
	 */
	private Inventory convertInventory(@NotNull PlayerInventory other) {

		int validSize = other.getStorageContents().length;
		int armorSize = other.getArmorContents().length;

		Inventory newInventory = plugin.getServer().createInventory(null, InventoryType.PLAYER);

		//Copy each item stack
		for(int i = 0; i < validSize; i++)
			newInventory.setItem(i, other.getItem(i));

		//Set armor contents while copying
		for(int i = 0; i < armorSize; i++) {
			newInventory.setItem(i+validSize, other.getArmorContents()[i]);
		}

		return newInventory;
	}

	/**
	 * Saves a player's inventory to the hashMap
	 * @param p player Object
	 * @param nameToSave String name to save the inventory under
	 */
	public void saveInventory(@NotNull Player p, @NotNull String nameToSave)
	{
		PlayerInventory inventory = p.getInventory();
		//Adds deep copy of savedInventory to hashmap
		savedInventories.put(nameToSave, convertInventory(inventory));
		DatabaseUtils.saveInventories(savedInventories, minigameName);
	}

	/**
	 * Helper method to force the database manager to save to a file
	 */
	public void forceSaveToFile() {
		DatabaseUtils.saveInventories(savedInventories, minigameName);
	}

	/**
	 * Gets a player's inventory
	 * @param nameOfInventory Name to retrieve the player's inventory under
	 * @param forgetInventory whether to forget the inventory once it is pulled
	 * @return Inventory object
	 */
	public Inventory getInventory(@NotNull String nameOfInventory, boolean forgetInventory)
	{
		//Forget the inventory option
		if(forgetInventory)
			return savedInventories.remove(nameOfInventory);

		return savedInventories.get(nameOfInventory);

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
	public boolean setPlayerInventory(Player toSet, String targetInventoryName) {

		PlayerInventory playerInv = toSet.getInventory();
		Inventory targetInv = savedInventories.get(targetInventoryName);

		if(targetInv==null)
			return false;

		toSet.getInventory().setContents(targetInv.getContents());
		toSet.updateInventory();
		return true;
	}
}
