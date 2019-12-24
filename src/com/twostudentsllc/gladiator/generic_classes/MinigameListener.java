package com.twostudentsllc.gladiator.generic_classes;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;

import com.twostudentsllc.gladiator.Main;

public abstract class MinigameListener implements Listener{
	
	protected Main plugin;
	
	protected MatchRound round;
	
	protected MapMatch match;
	
	public MinigameListener(Main plugin, MatchRound round)
	{
		this.plugin = plugin;
		this.round = round;
		registerEvent();
	}
	
	public MinigameListener(Main plugin, MapMatch match)
	{
		this.plugin = plugin;
		this.match = match;
		registerEvent();
	}
	
	public void registerEvent()
	{
		Bukkit.getPluginManager().registerEvents(this, plugin);
		Bukkit.broadcastMessage("Event registered.");
	}
	
	
	public abstract void unregisterEvent();
	
	public abstract void eventCalled(Event e);
}
