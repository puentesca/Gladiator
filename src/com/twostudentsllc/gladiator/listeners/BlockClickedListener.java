package com.twostudentsllc.gladiator.listeners;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import com.twostudentsllc.gladiator.Main;
import com.twostudentsllc.gladiator.managers.ClickableBlockManager;

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
		
		Block b = pEvent.getClickedBlock();
		ClickableBlockManager clickable = plugin.getGameManager().getClickableBlockManager();
		
		if(!clickable.clickedBlockExists(b.getLocation()))
		{
			System.out.println("Clicked block does not exist!");
			return;
		}
		String command = clickable.getClickedBlockCommand(b.getLocation());
		//Executes the command and omits the slash as its automatically added.
		player.performCommand(command.substring(1));
		
		System.out.println("Player clicked block and command: '" + command + "' was executed.");
	}
}
