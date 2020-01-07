package com.twostudentsllc.gladiator.global_listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import com.twostudentsllc.gladiator.Main;
import com.twostudentsllc.gladiator.global_managers.ClickableBlockManager;
import org.bukkit.inventory.EquipmentSlot;

/**
 * A listener
 * Copyright 2019 Casey Puentes. All rights reserved.
 * @author Casey Puentes
 *
 */
public class BlockClickedListener implements Listener{
	private Main plugin;
	public BlockClickedListener(Main plugin)
	{
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
		Bukkit.broadcastMessage("Event registered.");
	}
	
	public void unregisterEvent()
	{
		HandlerList.unregisterAll(this);
		Bukkit.broadcastMessage("Event unregistered.");
	}
	
	@EventHandler
	public void onBlockClick(PlayerInteractEvent e)
	{	
		eventCalled(e);
	}
	
	public void eventCalled(Event e)
	{
		PlayerInteractEvent pEvent = (PlayerInteractEvent)e;
		Player player = pEvent.getPlayer();

		//Ignore double action event when right clicking
		if(pEvent.getHand() != null && !pEvent.getHand().equals(EquipmentSlot.HAND))
			return;
		
		Block b = pEvent.getClickedBlock();

		//Filter out air
		if(b.getType() == Material.AIR)
			return;
		
		ClickableBlockManager clickable = plugin.getGameManager().getClickableBlockManager();
		
		
		
		if(b == null || !clickable.clickedBlockExists(b.getLocation()))
		{
			System.out.println("Clicked block does not exist!");
			return;
		}
		clickable.PlayerClickedBlock(player, b.getLocation());
		
		
	}
}
