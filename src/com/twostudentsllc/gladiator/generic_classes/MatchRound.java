package com.twostudentsllc.gladiator.generic_classes;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import com.twostudentsllc.gladiator.Main;
import com.twostudentsllc.gladiator.runnables.RoundTimelimitUpdater;

public abstract class MatchRound {
	protected Main plugin;
	
	//TODO: Add listener that prevents teammates from damaging each other and stops people in the warmup from shooting arrows or throwing stuff(Do the second part in the maps classes)
	
	/**
	 * The teams playing in this round
	 */
	protected ArrayList<Team> teams;
	
	/*
	 * The match that this round is taking place in
	 */
	protected MapMatch match;
	
	/**
	 * The original length of this round (In seconds)
	 */
	protected int roundTotalTimelimit;
	
	/**
	 * The amount of lives a player has 
	 */
	protected int livesPerPlayer;
	
	/**
	 * The time (in seconds) that a player must wait to respawn
	 */
	protected int respawnTime;
	
	/**
	 * Holds countdown respawn timers. Here so the current ones can be cancelled when a round is ended
	 */
	protected HashMap<Player, Countdown> respawnCountdowns;
	
	protected Countdown roundCountdown;
	
	protected ArrayList<MinigameListener> registeredListeners;
	
	public MatchRound(Main plugin, MapMatch match, ArrayList<Team> teams, int roundTotalTimeLimit, int livesPerPlayer)
	{
		this.plugin = plugin;
		this.match = match;
		this.teams = teams;
		this.roundTotalTimelimit = roundTotalTimeLimit;
		this.livesPerPlayer = livesPerPlayer;
		this.respawnTime = 7; //FIXME: Make this time customizable! It is not taken in by the constructor
		registeredListeners = new ArrayList<MinigameListener>();
		respawnCountdowns = new HashMap<Player, Countdown>();
		startRound();
	}
	
	/**
	 * Creates listeners for all events that will need to be handled by this round
	 */
	public abstract void registerRoundListeners();
	
	/**
	 * Destroys all of the listeners to increase server performance
	 */
	public void unregisterRoundListeners() {
		for(MinigameListener ml : registeredListeners)
		{
			ml.unregisterEvent();
		}
		
	}
	
	/**
	 * Ends the round.  Responsible for calling unregisterListeners() and stopping all respawn countdowns as well as updating mysql stats for players
	 */
	public abstract void endRound();
	
	/**
	 * Starts the round. Responsible for calling registerListeners()
	 */
	public abstract void startRound();
	
	/**
	 * Receieves the round's time limits time (in seconds) every second and handles a reaction
	 * @param time The amount of time left in the round
	 */
	public abstract void handleTimeRemaining(int time);
	
	/**
	 * Called whenever the countdown for a player to respawn is incremented by one second. Need to add and remove countdown from respawnCountdowns
	 * @param timer The Countdown tracking the respawn time left
	 * @param time The time left on the countdown
	 * @param p The player the countdown is tracking
	 */
	public abstract void handleRespawnTimeRemaining(int time, Player p);
	
	/**
	 * Called when a player in this round has been killed by another player
	 * @param e The event from the PlayerDamageEvent
	 * @param killed The player who was killed
	 * @param killer The player who killed the other player
	 */
	public abstract void handlePVPDeathEvent(Event e, Player killed, Player killer);
	
	/**
	 * Called when a player in this round has been killed by something other than a player
	 * @param e The event from the PlayerDamageEvent
	 * @param killed The player who was killed
	 * @param entity The player who killed the other player
	 */
	public abstract void handleNaturalPlayerDeathEvent(Event e, Player killed, Entity killer);
	
	/**
	 * Respawns a player
	 * @param p The player to respawn
	 */
	public abstract void respawnPlayer(Player p);
	
	/**
	 * Starts the time limit coundown for the round
	 */
	public void startTimelimitCountdown()
	{
		Countdown c;
		
		Runnable task = new RoundTimelimitUpdater(plugin, this);
		c = new Countdown(plugin, roundTotalTimelimit, task, true);
		roundCountdown = c;
	}
	
	public Countdown getTimelimitCountdown()
	{
		return roundCountdown;
	}
	
	public HashMap<Player, Countdown> getRespawnCountdowns()
	{
		return respawnCountdowns;
	}
	
	public Countdown getPlayerRespawnCountdown(Player p)
	{
		return respawnCountdowns.get(p);
	}
	
	/**
	 * Checks to see if the given player is currently in this round
	 * @param p The player to check for
	 * @return True if the player is in this round
	 */
	public boolean hasPlayer(Player p)
	{
		for(Team t : teams)
		{
			if(t.containsPlayer(p))
				return true;
		}
		return false;
	}
	
	/**
	 * Similar to hasPlayer(Player p), but instead returns null if the player is not in this round and a Team if the player is found
	 * @param p The player to check for
	 * @return True if the player is in this round
	 */
	public Team getTeam(Player p)
	{
		for(Team t : teams)
		{
			if(t.containsPlayer(p))
				return t;
		}
		return null;
	}
	
	/**
	 * Called when a player in this round has died. Responsible for calling playerEliminated() when a played has been eliminated
	 * @param p
	 */
	public abstract void playerDied(Player p);
	
	/**
	 * Called by playerDied() when a player has been eliminated
	 * @param p
	 */
	public abstract void playerEliminated(Player p);
	/**
	 * Called when a player in this round has earned a kill
	 * @param p
	 */
	public abstract void playerEarnedKill(Player p);
}
