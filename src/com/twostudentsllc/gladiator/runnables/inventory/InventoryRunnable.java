package com.twostudentsllc.gladiator.runnables.inventory;

import com.twostudentsllc.gladiator.utils.vote.Vote;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import com.twostudentsllc.gladiator.Main;

public abstract class InventoryRunnable {
	protected Main plugin;
	protected Vote curVote;
	
	public InventoryRunnable(Main plugin)
	{
		this.plugin = plugin;
	}
	public InventoryRunnable(Main plugin, Vote curVote)
	{
		this.curVote = curVote;
	}
	
	//Runs the event
	public abstract void runEvent(InventoryClickEvent e, Player p);
}
