package com.twostudentsllc.gladiator.runnables.game.round;

import com.twostudentsllc.gladiator.Main;
import com.twostudentsllc.gladiator.generic_game.handlers.MatchRound;

/**
 * A runnable class that sends the current countdown time to the map round
 * Copyright 2019 Casey Puentes. All rights reserved.
 * @author Casey Puentes
 *
 */
public class RoundTimelimitUpdater implements Runnable {

	/**
	 * The round that the time left should be sent to
	 */
	private MatchRound round;
	
	//TODO: Change this class so it is handled by the MatchRound class
	private Main plugin;
	
	public RoundTimelimitUpdater(Main plugin, MatchRound round)
	{
		this.plugin = plugin;
		this.round = round;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		sendTimeLeft();
	}
	
	/**
	 * Sends the time left to the current round
	 */
	public void sendTimeLeft()
	{
		int timeLeft = round.getTimelimitCountdown().getSecondsLeft();
		round.handleTimeRemaining(timeLeft);
	}

}