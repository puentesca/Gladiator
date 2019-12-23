package com.twostudentsllc.gladiator.generic_classes;

import java.util.ArrayList;

import org.bukkit.entity.Player;

import com.twostudentsllc.gladiator.Main;
import com.twostudentsllc.gladiator.runnables.RoundTimelimitUpdater;

public abstract class MatchRound {
	protected Main plugin;
	
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
	
	//TODO: Add total lives per player in match and methods to add deaths, respawn players, and start
	//countdown for respawn. Also need to define respawn delay.
	
	protected Countdown roundCountdown;
	
	protected ArrayList<MinigameListener> registeredListeners;
	
	public MatchRound(Main plugin, MapMatch match, ArrayList<Team> teams, int roundTotalTimeLimit, int livesPerPlayer)
	{
		this.plugin = plugin;
		this.match = match;
		this.teams = teams;
		this.roundTotalTimelimit = roundTotalTimeLimit;
		this.livesPerPlayer = livesPerPlayer;
		registeredListeners = new ArrayList<MinigameListener>();
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
	 * Ends the round.  Responsible for calling unregisterListeners()
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
