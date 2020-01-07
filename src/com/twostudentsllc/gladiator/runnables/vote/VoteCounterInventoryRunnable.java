package com.twostudentsllc.gladiator.runnables.vote;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import com.twostudentsllc.gladiator.Main;
import com.twostudentsllc.gladiator.runnables.inventory.InventoryRunnable;
import com.twostudentsllc.gladiator.utils.vote.Vote;

/**
 * Test Inventory Runnable
 * Copyright 2020 Casey Puentes. All rights reserved.
 * @author Casey Puentes
 *
 */
public class VoteCounterInventoryRunnable extends InventoryRunnable{
	public VoteCounterInventoryRunnable(Main plugin, Vote vote)
	{
		super(plugin, vote);
	}
	
	public void runEvent(InventoryClickEvent e, Player p)
	{
		//Counts the players vote
		String vote = e.getCurrentItem().getItemMeta().getDisplayName();
		curVote.vote(p, vote);
	}
}
