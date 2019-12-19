package com.twostudentsllc.gladiator.arenas;

import java.util.ArrayList;

import org.bukkit.entity.Player;

import com.twostudentsllc.gladiator.Main;
import com.twostudentsllc.gladiator.supers.Countdown;

/**
 * An interface for each round in an arena.
 * Copyright 2019 Casey Puentes. All rights reserved.
 * @author Casey Puentes
 *
 */

//TODO: Add timelimit variables

public abstract class MapRound {
	/**
	 * Contains the status of the current round
	 * @author Casey Puentes
	 *
	 */
	public static enum STATUS {
		WAITING, LOADING, IN_PROGRESS, COMPLETED, ERROR
	};
	
	private Main plugin;
	
	/**
	 * Holds the starting time limit of the round, in seconds
	 */
	private int timeLimit;
	
	/**
	 * Holds the time limit countdown for when a round is running.
	 */
	private Countdown timelimitCountdown;
	
	private ArrayList<Runnable> countdownTasks;
	
	private ArrayList<Integer> countdownTasksThresholds;
	
	
	//TODO: Add logic to accept the teams and players
	//Also add appropriate variables
	public MapRound(Main plugin, int timeLimit, ArrayList<Runnable> countdownTasks, ArrayList<Integer> countdownTasksThresholds)
	{
		this.plugin = plugin;
		this.timeLimit = timeLimit;
		this.countdownTasks = countdownTasks;
		this.countdownTasksThresholds = countdownTasksThresholds;
	}
	/**
	 * Gets the winner of the round
	 * @return
	 */
	public abstract Player getWinner();
	/**
	 * Gets the current status of the round
	 * @return The status of the round
	 */
	public abstract STATUS getStatus();
	/**
	 * Starts a round with the players and teams assigned
	 * @return True if the round was successfully started
	 */
	public abstract boolean startRound();
	
	/**
	 * Starts the time limit coundown for the round
	 */
	public void startTimelimitCountdown()
	{
		Countdown c;
		c = new Countdown(plugin, timeLimit, countdownTasks, countdownTasksThresholds, true);
		timelimitCountdown = c;
	}
	/**
	 * Stops the rounds timelimit countdown
	 */
	public void stopTimelimitCountdown()
	{
		timelimitCountdown.stopCountdown();
	}
	
	/**
	 * Ends a round
	 * @return True if the round was successfully ended
	 */
	public abstract boolean endRound();
}
