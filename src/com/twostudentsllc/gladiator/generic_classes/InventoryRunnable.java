package com.twostudentsllc.gladiator.generic_classes;

import org.bukkit.entity.Player;

import com.twostudentsllc.gladiator.Main;

public abstract class InventoryRunnable {
	private Main plugin;
	
	public InventoryRunnable(Main plugin)
	{
		this.plugin = plugin;
	}
	
	//Runs the event
	public abstract void runEvent(Player p);
}
