package com.twostudentsllc.gladiator.listeners.gladiator;

import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import com.twostudentsllc.gladiator.Main;
import com.twostudentsllc.gladiator.generic_classes.MatchRound;
import com.twostudentsllc.gladiator.generic_classes.MinigameListener;


public class DeathListener extends MinigameListener{
	
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
	public void onDeath(EntityDamageEvent e)
	{	
		eventCalled(e);
	}
	
	@Override
	public void eventCalled(Event e)
	{
		//Making sure its the correct type of event
		if(!(e instanceof EntityDamageEvent))
			return;
		
		EntityDamageEvent ede = (EntityDamageEvent)e;
		if(!(ede.getEntity() instanceof Player))
		{
			System.out.println("Entity in damage event is not a player. Instead is: '" + ede.getEntity() + "'");
			return;
		}
		
		Player damagee = (Player)ede.getEntity();
		double damageDone = ede.getFinalDamage();
		boolean playerWillDie = false;
		if(damageDone >= damagee.getHealth())
			playerWillDie = true;
		
		//If the player will die
		if(playerWillDie)
		{	
			//Checks to see if the player was killed by another player
			EntityDamageByEntityEvent damageEvent = (EntityDamageByEntityEvent)e;
			Entity damager = damageEvent.getDamager();
			
			//If the damager is a player and the player is in the round
			if(damager instanceof Player && round.hasPlayer((Player)damager))
			{
				round.handlePVPDeathEvent(e, damagee, (Player)damager);
				return;
			}
			//If the damager is any other entity
			else
			{
				round.handleNaturalPlayerDeathEvent(e, damagee, damager);
			}
		}
		
		
		
		
		

		
	}
}
