package com.twostudentsllc.gladiator.arenas;

import org.bukkit.entity.Player;

/**
 * An interface for each round in an arena.
 * Copyright 2019 Casey Puentes. All rights reserved.
 * @author Casey Puentes
 *
 */
public interface GladiatorRound {
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
	public Player getWinner();
	/**
	 * Gets the current status of the round
	 * @return The status of the round
	 */
	public STATUS getStatus();
	/**
	 * Starts a round with the players and teams assigned
	 * @return True if the round was successfully started
	 */
	public boolean startRound();
	
	/**
	 * Ends a round
	 * @return True if the round was successfully ended
	 */
	public boolean endRound();
}
