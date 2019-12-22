package com.twostudentsllc.gladiator.generic_classes;

import java.util.ArrayList;

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
	
	//TODO: Add total lives per player in match and methods to add deaths, respawn players, and start
	//countdown for respawn. Also need to define respawn delay.
	
	protected Countdown roundCountdown;
	
	public MatchRound(Main plugin, MapMatch match, ArrayList<Team> teams, int roundTotalTimeLimit)
	{
		this.plugin = plugin;
		this.match = match;
		this.teams = teams;
		this.roundTotalTimelimit = roundTotalTimeLimit;
		startRound();
	}
	
	/**
	 * Creates listeners for all events that will need to be handled by this round
	 */
	public abstract void registerListeners();
	
	/**
	 * Destroys all of the listeners to increase server performance
	 */
	public abstract void unregisterListeners();
	
	/**
	 * Ends the round
	 */
	public abstract void endRound();
	
	/**
	 * Starts the round
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
}
