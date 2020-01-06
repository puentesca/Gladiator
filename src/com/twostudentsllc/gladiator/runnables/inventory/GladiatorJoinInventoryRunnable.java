package com.twostudentsllc.gladiator.runnables.inventory;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.twostudentsllc.gladiator.Main;
import com.twostudentsllc.gladiator.generic_classes.InventoryRunnable;

/**
 * Test Inventory Runnable
 * Copyright 2020 Casey Puentes. All rights reserved.
 * @author Casey Puentes
 *
 */
public class GladiatorJoinInventoryRunnable extends InventoryRunnable{
	public GladiatorJoinInventoryRunnable(Main plugin)
	{
		super(plugin);
	}
	
	public void runEvent(Player p)
	{
		//FIXME: CHange to console and override permissions
		p.performCommand("gladiator join gladiator testmap");
	}
}
