package com.twostudentsllc.gladiator.listeners.gladiator;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.twostudentsllc.gladiator.Main;
import com.twostudentsllc.gladiator.generic_classes.MatchRound;
import com.twostudentsllc.gladiator.generic_classes.MinigameListener;

public class DeathListener extends MinigameListener{
	
	//FIXME: Change it so this listens for lowering of health and if its to the point where the player will die,
	//cancel the damage, set the health to full, and respawn the player
	public DeathListener(Main plugin, MatchRound round)
	{
		super(plugin, round);	
	}
	
	@Override
	public void unregisterEvent()
	{
		HandlerList.unregisterAll(this);
		Bukkit.broadcastMessage("Event unregistered.");
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent e)
	{
		//TODO: Make it so events are only triggered by players in the current round
		//If the player is in the round this event is tracking
		if(round.hasPlayer(e.getEntity().getPlayer()))
			eventCalled(e);
	}
	
	@Override
	public void eventCalled(Event e)
	{
		PlayerDeathEvent pd = (PlayerDeathEvent)e;
		Player died = pd.getEntity().getPlayer();
		round.playerDied(died);
		
		Player killer = pd.getEntity().getPlayer().getKiller();
		if(!(killer instanceof Player) || killer == null || !round.hasPlayer(killer))
		{
			System.out.println("Killer not detected.");
			return;
		}
		round.playerEarnedKill(killer);
	}
}
