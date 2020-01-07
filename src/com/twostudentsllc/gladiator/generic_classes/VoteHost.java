package com.twostudentsllc.gladiator.generic_classes;
/**
 * Generic voting interface that is used to send the winner at the end of a vote.
 * Copyright 2020 Casey Puentes. All rights reserved.
 * @author Casey Puentes
 *
 */
public interface VoteHost {
	public void votingFinished(String winner);
}
