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
public class TestInventoryRunnable extends InventoryRunnable{
	public TestInventoryRunnable(Main plugin)
	{
		super(plugin);
	}
	
	public void runEvent(Player p)
	{
		Bukkit.broadcastMessage("Player clicked the inventory slot!");
	}
}
