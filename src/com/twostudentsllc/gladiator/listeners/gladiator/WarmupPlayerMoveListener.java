package com.twostudentsllc.gladiator.listeners.gladiator;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerMoveEvent;

import com.twostudentsllc.gladiator.Main;
import com.twostudentsllc.gladiator.generic_classes.MapMatch;
import com.twostudentsllc.gladiator.generic_classes.MinigameListener;


public class WarmupPlayerMoveListener extends MinigameListener{
	
	public WarmupPlayerMoveListener(Main plugin, MapMatch match)
	{
		super(plugin, match);
	}
	
	@Override
	public void unregisterEvent()
	{
		HandlerList.unregisterAll(this);
		Bukkit.broadcastMessage("Event unregistered.");
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e)
	{	
		eventCalled(e);
	}
	
	@Override
	public void eventCalled(Event e)
	{
		PlayerMoveEvent event = ((PlayerMoveEvent) e);
		Player p = event.getPlayer();
        //Location from = event.getFrom();
        double xfrom = event.getFrom().getX();
        double yfrom = event.getFrom().getY();
        double zfrom = event.getFrom().getZ();
        double xto = event.getTo().getX();
        double yto = event.getTo().getY();
        double zto = event.getTo().getZ();
        if (!(xfrom == xto && yfrom == yto && zfrom == zto)) {
        	match.sendPlayerToSpawn(p, false);
        }
		
	}
}
