package com.twostudentsllc.gladiator.runnables.inventory;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import com.twostudentsllc.gladiator.Main;

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
	
	public void runEvent(InventoryClickEvent e, Player p)
	{
		//FIXME: CHange to console and override permissions
		p.performCommand("gladiator join gladiator testmap");
	}
}
