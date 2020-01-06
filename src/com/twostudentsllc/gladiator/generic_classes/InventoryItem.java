package com.twostudentsllc.gladiator.generic_classes;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.twostudentsllc.gladiator.Main;

public class InventoryItem {
	
	private Main plugin;
	private ItemStack item;
	private int location;
	/**
	 * Creates an item that will be used for an inventory
	 * @param itemType The material of the item to be created
	 * @param displayName The name of the item
	 * @param lore The lore to be on the item
	 * @param amount The amount of the item to be created
	 * @param glow Whether or not the item should glow with enchantment
	 * @param location The index of the slot the item should be placed in 
	 */
	public InventoryItem(Main plugin, Material itemType, String displayName, ArrayList<String> lore, int amount, boolean glow, int location)
	{
		this.plugin = plugin;
		createInventoryItem(itemType, displayName, lore, amount, glow);
		this.location = location;
	}
	
	/**
	 * Creates an item that will be used for a inventory GUI
	 * @param itemType The material of the item to be created
	 * @param displayName The name of the item
	 * @param lore The lore to be on the item
	 * @param amount The amount of the item to be created
	 * @param glow Whether or not the item should glow with enchantment
	 * @return The item
	 */
	public void createInventoryItem(Material itemType, String displayName, ArrayList<String> lore, int amount, boolean glow)
	{
		ItemStack item = new ItemStack(itemType, amount);
		
		GlowEnchant glowEnchant = new GlowEnchant(plugin);
		
		//Removes the enchantment if it already exists
		if(item.containsEnchantment(glowEnchant))
			item.removeEnchantment(glowEnchant);
		
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(displayName);
		if(lore != null)
			meta.setLore(lore);
		if(glow)
		{
			meta.addEnchant(glowEnchant, 1, true);
		}
		item.setItemMeta(meta);
		
		this.item = item;
	}
	
	public ItemStack getItem()
	{
		return item;
	}
	
	public int getLocation()
	{
		return location;
	}
}
