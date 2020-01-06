package com.twostudentsllc.gladiator.generic_classes;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import com.twostudentsllc.gladiator.Main;

/**
 * GUI Inventory
 * Copyright 2019 Casey Puentes. All rights reserved.
 * @author Casey Puentes
 *
 */
public class GUIInventory implements InventoryHolder, Listener {

	private Main plugin;
	private Inventory guiInv;
	private String name;
	private HashMap<Integer, InventoryRunnable> invListenerMap;
	public GUIInventory(Main plugin, String inventoryName, HashMap<InventoryRunnable, InventoryItem> items, int rows)
	{
		if(rows == 0)
			throw new IllegalArgumentException("You cannot have 0 rows in an inventory!");
		this.plugin = plugin;
		name = inventoryName;
		rows *= 9;
		createGUIInventory(inventoryName, items, rows);
		Bukkit.getPluginManager().registerEvents(this, plugin); //Registers the inventory listener
	}
	
	/**
	 * Creates an inventory with the specified items in the spots desired
	 * @param items
	 * @param rows
	 */
	private void createGUIInventory(String name, HashMap<InventoryRunnable, InventoryItem> items, int rows)
	{
		guiInv = Bukkit.createInventory(this, rows, name);
		invListenerMap = new HashMap<Integer, InventoryRunnable>();
		//Adds the items to the inventory
		for(InventoryRunnable i : items.keySet())
		{
			ItemStack item = items.get(i).getItem();
			if(item == null)
				continue;
			int itemLocation = items.get(i).getLocation();
			invListenerMap.put(itemLocation, i);
			guiInv.setItem(itemLocation, item);
			System.out.println("Just set item: " + item.getType() + " at slot: " + itemLocation);
		}
		
		
	}
	
	@EventHandler
	public void invClicked(InventoryClickEvent e)
	{
		Inventory clickedInv = e.getClickedInventory();
		//If the player clicked air instead of the inventory
		if(clickedInv == null)
			return;
		Player clicker = (Player)e.getWhoClicked();
		ItemStack clickedItem = e.getCurrentItem();
		int location = e.getRawSlot();
		e.setCancelled(true);
		if(clickedInv.equals(guiInv))
		{
			System.out.println("Size: " + invListenerMap.size());
			//If the slot clicked has a runnable associated with it, run the event
			if(invListenerMap.containsKey(location))
			{
				invListenerMap.get(location).runEvent(clicker);
			}
			System.out.println("Item clicked does not have an inventoryrunnable");
		}
		else
		{
			System.out.println("Clicked inv is not the same as the listeners.");
		}
	}
	
	/**
	 * Changes an item in the inventory
	 * @param location The index of the location that should be changed
	 * @param newItem The new item to be set
	 */
	public void changeInventoryItem(int location, ItemStack newItem)
	{
		guiInv.setItem(location, newItem);
	}
		
	@Override
	public Inventory getInventory() {
		return guiInv;
	}
	
	public String getName()
	{
		return name;
	}
}
