package com.twostudentsllc.gladiator.global_listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerQuitEvent;

import com.twostudentsllc.gladiator.Main;
import com.twostudentsllc.gladiator.generic_game.handlers.MapMatch;
import com.twostudentsllc.gladiator.generic_game.listeners.MinigameListener;

/**
 * Listeners for a player to disconnect in a match.
 * Copyright 2020 Casey Puentes. All rights reserved.
 * 
 * @author Casey Puentes
 *
 */
public class PlayerDisconnectListener extends MinigameListener{
	public PlayerDisconnectListener(Main plugin, MapMatch match)
	{
		super(plugin, match);
		Bukkit.getPluginManager().registerEvents(this, plugin);
		Bukkit.broadcastMessage("Event registered.");
	}
	
	public void unregisterEvent()
	{
		HandlerList.unregisterAll(this);
		Bukkit.broadcastMessage("Event unregistered.");
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerQuitEvent e)
	{	
		eventCalled(e);
	}
	
	public void eventCalled(Event e)
	{
		Player player = ((PlayerQuitEvent)e).getPlayer();
		match.playerDisconnected(player);
			
	}
}
