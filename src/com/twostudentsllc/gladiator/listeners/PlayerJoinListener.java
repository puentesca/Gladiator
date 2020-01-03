package com.twostudentsllc.gladiator.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.twostudentsllc.gladiator.Main;

public class PlayerJoinListener implements Listener{
	private Main plugin;
	public PlayerJoinListener(Main plugin)
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
	public void onPlayerJoin(PlayerJoinEvent e)
	{	
		eventCalled(e);
	}
	
	public void eventCalled(Event e)
	{
		Player player = ((PlayerJoinEvent)e).getPlayer();
		plugin.getMysqlManager().getCommunicator().createPlayer(player.getUniqueId(), player);
	}
}
