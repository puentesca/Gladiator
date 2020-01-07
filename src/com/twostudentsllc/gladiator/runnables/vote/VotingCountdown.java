package com.twostudentsllc.gladiator.runnables.vote;

import com.twostudentsllc.gladiator.Main;
import com.twostudentsllc.gladiator.utils.vote.Vote;
/**
 * Copyright 2020 Casey Puentes. All rights reserved.
 * @author Casey Puentes
 *
 */
public class VotingCountdown implements Runnable {

	private Main plugin;
	private Vote vote;
	
	public VotingCountdown(Main plugin, Vote vote)
	{
		this.plugin = plugin;
		this.vote = vote;
	}
	
	@Override
	public void run() {
		sendTimeLeft();

	}

	public void sendTimeLeft()
	{
		vote.handleVotingTimeLeft(vote.getVotingCountdown().getSecondsLeft());
	}
}
