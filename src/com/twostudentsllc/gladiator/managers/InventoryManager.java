package com.twostudentsllc.gladiator.managers;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.twostudentsllc.gladiator.Main;

public class InventoryManager {
	
	HashMap<UUID, Inventory> inventoriesSaved;
	
	private Main plugin;
	public InventoryManager(Main plugin)
	{
		this.plugin = plugin;
		inventoriesSaved = new HashMap<UUID, Inventory>();
	}
	
	public void saveInventory(Player p)
	{
		//Get player inventory, create deep copy, set it the inventory variable
		//get player UUID
		//p.getUniqueId();
		
		//inventoriesSaved.put(<PlayerUUID>, <PlayerInv>);
	}
	
	public Inventory getInventory(Player p, boolean forgetInventory)
	{
		//if the inventory should be removed from the database (if (forgetInventory)), then remove and return it using UUID.
		//inventoriesSaved.get(players UUID);
		return null; //Remove this line and replace it so that it returns the 
					 //players saved inventory from the inventoriesSaved
	}
	
	
}
