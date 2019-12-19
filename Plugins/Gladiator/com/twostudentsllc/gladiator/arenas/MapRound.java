package com.twostudentsllc.gladiator.arenas;

import org.bukkit.entity.Player;

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
	 * Ends a round
	 * @return True if the round was successfully ended
	 */
	public abstract boolean endRound();
}
